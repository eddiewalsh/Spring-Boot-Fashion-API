package zelora.api.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zelora.api.helper.LinkFactory;
import zelora.api.helper.QRFactory;
import zelora.api.model.Customer;
import zelora.api.model.Discount;
import zelora.api.services.CustomerService;
import zelora.api.services.DiscountService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/zelora/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "/customers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getDiscountsByCustomerID(@PathVariable Integer id, @PageableDefault(size = 10) Pageable pageable){
        Optional<Customer> customer = customerService.findOne(id);

        if(!customer.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find customer with the id " + id);
        }

        Page<Discount> discounts = discountService.getDiscountsByCustomer(customer.get(),pageable);

        Link selfLink = LinkFactory.getDiscountsByCustomerIDSelfLink(id);
        Link customerLink = LinkFactory.getCustomerByIDLink(id);

        CollectionModel<Discount> result = CollectionModel.of(discounts, selfLink, customerLink);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getDiscountsByID(@PathVariable Integer id){
        Optional<Discount> discount = discountService.getDiscountByID(id);

        if(!discount.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find discount with the id" + id);
        }

        Link selfLink = LinkFactory.getDiscountByIDSelfLink(id);
        Link customerLink = LinkFactory.getCustomerByIDLink(discount.get().getCustomerId().getCustomerId());

        EntityModel<Discount> result = EntityModel.of(discount.get(), selfLink, customerLink);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{discountID}/qrcode")
    public ResponseEntity<?> getDiscountQRCode(@PathVariable Integer discountID) throws Exception {
        Optional<Discount> discountOptional = discountService.getDiscountByID(discountID);
        if(!discountOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find discount with the id: " + discountID);
        }
        Discount discount = discountOptional.get();
        if(discount.isRedeemed()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot produce for a redeemed discount");
        }

        BufferedImage discountQRCode = QRFactory.generateQRCodeImage(discount.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(discountQRCode, "png", baos);
        Image QRCode = Image.getInstance(baos.toByteArray());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();
        document.add(QRCode);
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String discount_ID = discount.getDiscountId().toString();
        headers.setContentDispositionFormData("filename", discount_ID + "invoice.pdf");

        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/birthday/{email}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> assignBirthdayDiscountCustomer(@PathVariable String email) {

        Optional<Customer> customerOptional = customerService.getCustomerByEmail(email);

        if(!customerOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find customer with this email " + email);
        }

        Customer customer = customerOptional.get();

        Discount discount = discountService.GenerateBirthdayDiscount(customer);

        Link selfLink = linkTo(methodOn(DiscountController.class)
                .assignSustainabilityDiscountCustomer(email)).withSelfRel();

        Link discountLink = LinkFactory.getDiscountByIDLink(discount.getDiscountId());

        Link customerLink = LinkFactory.getCustomerByEmailLink(email);


        EntityModel<Discount> results = EntityModel.of(discount, selfLink, discountLink, customerLink);

        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sustainable/{email}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> assignSustainabilityDiscountCustomer(@PathVariable String email) {

        Optional<Customer> customerOptional = customerService.getCustomerByEmail(email);

        if(!customerOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find customer with this email " + email);
        }

        Customer customer = customerOptional.get();

        Discount discount = discountService.GenerateSustainabilityDiscount(customer);

        Link selfLink = linkTo(methodOn(DiscountController.class)
                .assignBirthdayDiscountCustomer(email)).withSelfRel();

        Link discountLink = LinkFactory.getDiscountByIDLink(discount.getDiscountId());

        Link customerLink = LinkFactory.getCustomerByEmailLink(email);


        EntityModel<Discount> results = EntityModel.of(discount, selfLink, discountLink, customerLink);

        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/first/{email}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> assignFirstPurchaseDiscountCustomer(@PathVariable String email) {

        Optional<Customer> customerOptional = customerService.getCustomerByEmail(email);

        if(!customerOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find customer with this email " + email);
        }

        Customer customer = customerOptional.get();

        Discount discount = discountService.GenerateFirstPurchaseDiscount(customer);

        Link selfLink = linkTo(methodOn(DiscountController.class)
                .assignFirstPurchaseDiscountCustomer(email)).withSelfRel();

        Link discountLink = LinkFactory.getDiscountByIDLink(discount.getDiscountId());

        Link customerLink = LinkFactory.getCustomerByEmailLink(email);


        EntityModel<Discount> results = EntityModel.of(discount, selfLink, discountLink, customerLink);

        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tenth/{email}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> assignTenthPurchaseDiscountCustomer(@PathVariable String email) {

        Optional<Customer> customerOptional = customerService.getCustomerByEmail(email);

        if(!customerOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find customer with this email " + email);
        }

        Customer customer = customerOptional.get();

        Discount discount = discountService.GenerateTenthPurchaseDiscount(customer);

        Link selfLink = linkTo(methodOn(DiscountController.class)
                .assignTenthPurchaseDiscountCustomer(email)).withSelfRel();

        Link discountLink = LinkFactory.getDiscountByIDLink(discount.getDiscountId());

        Link customerLink = LinkFactory.getCustomerByEmailLink(email);


        EntityModel<Discount> results = EntityModel.of(discount, selfLink, discountLink, customerLink);

        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }
}
