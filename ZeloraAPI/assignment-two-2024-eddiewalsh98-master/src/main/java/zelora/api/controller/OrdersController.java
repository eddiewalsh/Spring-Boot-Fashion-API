package zelora.api.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.aspectj.weaver.ast.Or;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import zelora.api.helper.LinkFactory;
import zelora.api.model.*;
import zelora.api.helper.QRFactory;
import zelora.api.services.*;

import javax.imageio.ImageIO;
import javax.swing.text.html.Option;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/zelora/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private SustainabilityFactService factService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private ResourceLoader resourceLoader;

    private List<String> orderStatus = Arrays.asList("Processing", "Shipped", "Delivered");

    @RequestMapping(method = RequestMethod.POST, value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> addOrder(@RequestBody Orders orders) {
        if(orders == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The order being submitted is null");
        }

        Link customerLink = LinkFactory.getCustomerByEmailLink(orders.getCustomerId().getEmail());
        Link allOrdersLink = LinkFactory.getAllOrdersLink(PageRequest.of(0, 10));

        orders.setOrderStatus("Processing");

        if(orders.getDiscountUsed().equals(false)){
            ordersService.SaveOrder(orders);
            EntityModel<Orders> results = EntityModel.of(orders, customerLink,allOrdersLink);
            return ResponseEntity.status(HttpStatus.CREATED).body(results);
        }

        if(orders.getDiscount().isRedeemed()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Discount Being Used Is Already Redeemed");
        }

        orders.setTotalAmount(ordersService.calculateDiscountedPrice(orders.getDiscount(), orders));
        ordersService.SaveOrder(orders);

        Optional<Discount> discount = discountService.getDiscountByID(orders.getDiscount().getDiscountId());
        if(!discount.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Discount Could Not Be Found");
        }

        discount.get().setRedeemed(true);
        discountService.SaveDiscount(discount.get());

        Link discountLink = LinkFactory.getDiscountByIDLink(discount.get().getDiscountId());

        EntityModel<Orders> results = EntityModel.of(orders, customerLink,discountLink,allOrdersLink);

        if(orders.getCustomerId().getOrdersList().size() == 1){
            discountService.GenerateFirstPurchaseDiscount(orders.getCustomerId());
        }

        if(orders.getCustomerId().getOrdersList().size() == 10){
            discountService.GenerateTenthPurchaseDiscount(orders.getCustomerId());
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }


    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/status/{newStatus}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer id, @PathVariable String newStatus) {
        try
        {
            Optional<Orders> ordersOptional = ordersService.getOrderByID(id);
            if(!ordersOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find order with the id: " + id);
            }
            if(!orderStatus.contains(newStatus.trim())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Insuitable order status");
            }

            Orders order = ordersOptional.get();
            if(order.getOrderStatus().equals(newStatus)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Order status is already " + newStatus);
            }

            Customer customer = order.getCustomerId();
            BigDecimal currentSustainRating = customer.getSustainability();


            order.setOrderStatus(newStatus);
            customer.setSustainability(ordersService
                    .generatingCustomersNewSustainabilityRating(order, customer));

            BigDecimal newSustainRating = customer.getSustainability();

            ordersService.SaveOrder(order);
            customerService.saveCustomer(customer);

            if(currentSustainRating.compareTo(newSustainRating) > 0
                    || currentSustainRating.compareTo(newSustainRating) < 0){
                if(newSustainRating.compareTo(new BigDecimal(100.00)) == 0){
                    discountService.GenerateSustainabilityDiscount(customer);
                }
            }

            Link selfLink = linkTo(methodOn(OrdersController.class)
                    .updateOrderStatus(id, newStatus)).withSelfRel();

            Link customerLink = LinkFactory.getCustomerByIDLink(customer.getCustomerId());

            EntityModel<Orders> result = EntityModel.of(order, selfLink, customerLink);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error");
        }
    }

    @GetMapping(value = "/{orderID}/redeem/{discountCode}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> redeemDiscountForOrder(@PathVariable Integer orderID,@PathVariable Integer discountCode) {
        Optional<Orders> orderOptional = ordersService.getOrderByID(orderID);
        Optional<Discount> discountOptional = discountService.getDiscountByID(discountCode);
        if(!discountOptional.isPresent() || discountOptional.get().isRedeemed()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not find discount with this id");
        }
        if(!orderOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not find order with this id");
        }

        Discount discount = discountOptional.get();
        Orders newOrder = orderOptional.get();

        newOrder.setDiscount(discount);
        newOrder.setDiscountUsed(true);
        newOrder.setTotalAmount(ordersService.calculateDiscountedPrice(discount, newOrder));
        ordersService.SaveOrder(newOrder);
        discount.setRedeemed(true);
        discountService.SaveDiscount(discount);

        Link orderLink = LinkFactory.getOrderByIDLink(newOrder.getOrderId());
        Link discountLink = LinkFactory.getDiscountByIDLink(discount.getDiscountId());

        EntityModel<Orders> results = EntityModel.of(newOrder, orderLink, discountLink);

        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllOrders(@PageableDefault(size = 10)Pageable pageable) {
        Page<Orders> orders = ordersService.findAllWithPagination(pageable);
        for (final Orders order: orders) {
            Link orderLink = LinkFactory.getOrderByIDLink(order.getOrderId());
            Link customer = LinkFactory.getCustomerByIDLink(order.getCustomerId().getCustomerId());
            Link orderItemsLink = LinkFactory.getOrderItemByOrderIDLink(order.getOrderId());
            if(order.getDiscountUsed().equals(true)){
                Integer discountID = order.getDiscount().getDiscountId();
                Link discountLink = LinkFactory.getDiscountByIDLink(discountID);
                order.add(orderLink, customer, orderItemsLink, discountLink);
            } else{
                order.add(orderLink, customer, orderItemsLink);
            }
        }

        Link selfLink = LinkFactory.getAllOrdersSelfLink(pageable);

        CollectionModel<Orders> result = CollectionModel.of(orders, selfLink);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getOrderByID(@PathVariable Integer id) {
        Optional<Orders> order = ordersService.getOrderByID(id);

        if(!order.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find order with the ID " + id);
        }

        Integer customerID = order.get().getCustomerId().getCustomerId();
        Link selfLink = LinkFactory.getOrderByIDSelfLink(id);
        Link customerLink = LinkFactory.getCustomerByIDLink(customerID);
        Link orderItemsLink = LinkFactory.getOrderItemByOrderIDLink(id);
        Link allOrdersLink = LinkFactory.getAllOrdersLink(PageRequest.of(0, 10));

        if(order.get().getDiscountUsed().equals(true)){
            Integer discountID = order.get().getDiscount().getDiscountId();
            Link discountLink = LinkFactory.getDiscountByIDLink(discountID);

            EntityModel<Orders> result = EntityModel.of(order.get(), selfLink, customerLink, discountLink,
                    orderItemsLink,allOrdersLink);

            return ResponseEntity.ok(result);
        }

        EntityModel<Orders> result = EntityModel.of(order.get(), selfLink, customerLink,
                                                orderItemsLink,allOrdersLink);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/customers/{customerID}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getOrdersByCustomerID(@PathVariable Integer customerID,@PageableDefault(size = 10)Pageable pageable) {
        Page<Orders> orders = ordersService.findOrdersByCustomerID(customerID, pageable);
        for (final Orders order: orders) {
            Integer orderID = order.getOrderId();
            Link orderLink = LinkFactory.getOrderByIDLink(orderID);
            Link orderItemsLink = LinkFactory.getOrderItemByOrderIDLink(orderID);
            if(order.getDiscountUsed().equals(true)){
                Integer discountID = order.getDiscount().getDiscountId();
                Link discountLink = LinkFactory.getDiscountByIDLink(discountID);
                order.add(orderLink,discountLink,orderItemsLink);
            } else{
                order.add(orderLink,orderItemsLink);
            }
        }

        Link selfLink = LinkFactory.getOrdersByCustomerIDSelfLink(customerID);

        CollectionModel<Orders> result = CollectionModel.of(orders, selfLink);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{orderID}/items", produces =  {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getOrderItemsByOrderID(@PathVariable Integer orderID, @PageableDefault(size = 10)Pageable pageable){
        Page<Orderitem> orderItems = orderItemService.getOrderItemsByOrderID(orderID, pageable);
        for (final Orderitem items: orderItems) {
            Link orderItemLink = LinkFactory.getOrderItemByIDLink(items.getOrderItemId());
            Link productLink = LinkFactory.getProductByIDLink(items.getProductId().getProductId());

            items.add(orderItemLink, productLink);
        }

       Link selfLink = LinkFactory.getOrderItemByOrderIDSelfLink(orderID);
       Link orderLink = LinkFactory.getOrderByIDLink(orderID);

       CollectionModel<Orderitem> results = CollectionModel.of(orderItems, selfLink, orderLink);

       return ResponseEntity.ok(results);
    }




    @GetMapping(value = "/{orderID}/pdf")
    public ResponseEntity<?> getInvoiceForOrder(@PathVariable Integer orderID) throws Exception {
        Optional<Orders> ordersOptional = ordersService.getOrderByID(orderID);

        if(!ordersOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find order with the id " + orderID);
        }
        Orders order = ordersOptional.get();
        if(order.getOrderStatus().equals(orderStatus.get(0))){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order Status must be shipped or delivered for invoice");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Document document = new Document();

        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
        Font boldFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 14, BaseColor.BLACK);

        PdfPTable logo = getLogoForPDF();

        if(logo != null) {
            document.add(logo);
        }

        Paragraph invoice_no_paragraph = new Paragraph();
        Paragraph invoice_order_date = new Paragraph();
        Paragraph invoice_customer_name = new Paragraph();
        Paragraph invoice_payment_method = new Paragraph();
        Paragraph invoice_shipping_method = new Paragraph();
        Paragraph invoice_order_total = new Paragraph();

        Chunk invoicelabel = new Chunk("Invoice: ", boldFont);
        Chunk invoice_no = new Chunk(order.getOrderId().toString(), font);

        invoice_no_paragraph.add(invoicelabel);
        invoice_no_paragraph.add(invoice_no);
        document.add(invoice_no_paragraph);

        Chunk orderDateLabel = new Chunk("Order Date: ", boldFont);
        Chunk order_date = new Chunk(order.getOrderDate().toString(), font);

        invoice_order_date.add(orderDateLabel);
        invoice_order_date.add(order_date);
        document.add(invoice_order_date);

        Chunk customerNameLabel = new Chunk("Customer Name: ", boldFont);
        Chunk customerName = new Chunk(order.getCustomerId().getFirstName() + " " + order.getCustomerId().getLastName(), font);

        invoice_customer_name.add(customerNameLabel);
        invoice_customer_name.add(customerName);
        document.add(invoice_customer_name);

        Chunk paymentMethodLabel = new Chunk("Payment Method: ", boldFont);
        Chunk paymentMethodValue = new Chunk(order.getPaymentMethod(), font);

        invoice_payment_method.add(paymentMethodLabel);
        invoice_payment_method.add(paymentMethodValue);
        document.add(invoice_payment_method);

        Chunk shippingLabel = new Chunk("Shipping Method: ", boldFont);
        Chunk shipping_value = new Chunk(order.getShippingMethod(), font);

        invoice_shipping_method.add(shippingLabel);
        invoice_shipping_method.add(shipping_value);
        document.add(invoice_shipping_method);

        Chunk orderTotalLabel = new Chunk("Order Total: ", boldFont);
        Chunk order_total = new Chunk("£" + order.getTotalAmount().toString(), font);

        invoice_order_total.add(orderTotalLabel);
        invoice_order_total.add(order_total);
        document.add(invoice_order_total);
        document.add(new Paragraph("Order Items: ", boldFont));
        document.add(new Chunk(new LineSeparator()));

        addOrderItemsToPDF(document, order.getOrderitemList());

        document.newPage();

        Image qrCodeImage = createSustainableFactQRCode();
        document.add(qrCodeImage);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String invoiceName = order.getOrderId().toString();
        headers.setContentDispositionFormData("filename", invoiceName + "_invoice.pdf");

        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);

    }

    public PdfPTable getLogoForPDF() throws IOException, BadElementException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(50);
        Resource resource = resourceLoader.getResource("classpath:/static/assets/images/logo.png");
        byte[] imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());
        Image logo = Image.getInstance(imageBytes);
        logo.scaleToFit(80, 30);
        PdfPCell cell = new PdfPCell(logo, true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        return table;
    }

    public PdfPTable getProductThumbnailForPDF(Product product) throws IOException, BadElementException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(20);
        Resource resource = resourceLoader.getResource("classpath:/static/assets/images/thumbs/" + product.getProductId() + "/" + product.getFeatureImage());
        byte[] imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());
        Image logo = Image.getInstance(imageBytes);
        logo.scaleToFit(30, 30);
        PdfPCell cell = new PdfPCell(logo, true);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
        return table;
    }


    public void addOrderItemsToPDF(Document document, List<Orderitem> orderitems) throws DocumentException, IOException {
        Font font = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);
        Font boldFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 12, BaseColor.BLACK);
        for(Orderitem orderitem : orderitems) {
            Product product = orderitem.getProductId();

            PdfPTable rowTable = new PdfPTable(2);
            rowTable.setWidthPercentage(100);

            // Add thumbnail in the first cell
            PdfPCell thumbnailCell = new PdfPCell();
            PdfPTable thumbnailTable = getProductThumbnailForPDF(product);
            thumbnailCell.addElement(thumbnailTable);
            thumbnailCell.setBorder(PdfPCell.NO_BORDER);
            rowTable.addCell(thumbnailCell);

            // Add order item details in the second cell
            PdfPCell detailsCell = new PdfPCell();
            detailsCell.setBorder(PdfPCell.NO_BORDER);

            // Add product name
            Paragraph productName = new Paragraph();
            Chunk product_name_label = new Chunk("Product Name: ", boldFont);
            Chunk product_name = new Chunk(product.getProductName(), font);
            productName.add(product_name_label);
            productName.add(product_name);
            detailsCell.addElement(productName);

            // Add quantity
            Paragraph quantity = new Paragraph();
            Chunk quantity_label = new Chunk("Quantity: ", boldFont);
            Chunk quantity_amount = new Chunk(orderitem.getQuantity().toString(), font);
            quantity.add(quantity_label);
            quantity.add(quantity_amount);
            detailsCell.addElement(quantity);

            // Add price
            Paragraph price = new Paragraph();
            Chunk price_label = new Chunk("Price: ", boldFont);
            Chunk price_amount = new Chunk(orderitem.getSubtotal().toString(), font);
            price.add(price_label);
            price.add(price_amount);
            detailsCell.addElement(price);

            rowTable.addCell(detailsCell);

            document.add(rowTable);

            // Add separator line
            Chunk linebreak = new Chunk(new LineSeparator());
            document.add(linebreak);
        }
    }

    public Image createSustainableFactQRCode() throws Exception {
        SustainabilityFacts facts = factService.generateRandomFact();
        BufferedImage qrCodeImage = QRFactory.generateQRCodeImage(facts.getFact());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", baos);
        return Image.getInstance(baos.toByteArray());
    }

}
