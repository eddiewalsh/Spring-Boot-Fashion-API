package zelora.api.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import zelora.api.helper.LinkFactory;
import zelora.api.helper.QRFactory;
import zelora.api.model.*;
import zelora.api.services.ProductService;

import javax.imageio.ImageIO;
import javax.swing.text.html.parser.Entity;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/zelora/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getProductByID(@PathVariable Integer id){
        Optional<Product> product = productService.getProductByID(id);
        if(!product.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Could not find product with the id " + id);
        }
        Integer categoryID = product.get().getCategoryId().getCategoryId();
        Integer supplierID = product.get().getSupplierId().getSupplierId();


        Link selfLink = LinkFactory.getProductByIDSelfLink(product.get().getProductId());
        Link categoryLink = LinkFactory.getCategoryByIDLink(categoryID);

        Link supplierLink = LinkFactory.getSupplierByIDLink(supplierID);
        Link inventoryLink = LinkFactory.getInventoryByProductIDLink(id);

        Link orderItemLink = LinkFactory.getOrderItemsByProductIDLink(id);
        Link wishlistLink = LinkFactory.getWishlistByProductIDLink(id);

        Link reviewLink = LinkFactory.getReviewsByProductIDLink(product.get().getProductId());
        Link thumbLink = LinkFactory.getProductThumbnailImage(id);

        Link largeLink = LinkFactory.getProductLargeImage(id);
        Link allProductsLink = LinkFactory.getAllProductsLink();


        EntityModel<Product> results = EntityModel.of(product.get(), selfLink, categoryLink, supplierLink,
                                                    inventoryLink, reviewLink,orderItemLink, wishlistLink,
                                                    thumbLink, largeLink, allProductsLink);

        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllProducts(@PageableDefault(10) Pageable pageable) {
        Page<Product> products = productService.findAllWithPagination(pageable);
        for(final Product product : products) {
            Integer catID = product.getCategoryId().getCategoryId();
            Integer supplierID = product.getSupplierId().getSupplierId();

            Link singleProduct = LinkFactory.getProductByIDLink(product.getProductId());
            Link categoryLink = LinkFactory.getCategoryByIDLink(catID);

            Link supplierLink = LinkFactory.getSupplierByIDLink(supplierID);
            Link reviewLink = LinkFactory.getReviewsByProductIDLink(product.getProductId());

            Link inventoryLink = LinkFactory.getInventoryByProductIDLink(product.getProductId());
            Link orderItemLink = LinkFactory.getOrderItemsByProductIDLink(product.getProductId());

            Link wishlistLink = LinkFactory.getWishlistByProductIDLink(product.getProductId());
            Link thumbLink = LinkFactory.getProductThumbnailImage(product.getProductId());

            Link largeLink = LinkFactory.getProductLargeImage(product.getProductId());

            product.add(singleProduct, categoryLink, supplierLink, reviewLink,
                    inventoryLink, orderItemLink, wishlistLink, thumbLink, largeLink);
        }

        Link selfLink = LinkFactory.getAllProductsSelfLink();
        CollectionModel<Product> results = CollectionModel.of(products, selfLink);

        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/supplier/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getProductsBySupplierID(@PathVariable Integer id, @PageableDefault(10) Pageable pageable) {
        Page<Product> products = productService.getProductBySupplierID(id, pageable);
        for(final Product product : products) {
            Integer catID = product.getCategoryId().getCategoryId();
            Link singleProduct = LinkFactory.getProductByIDLink(product.getProductId());
            Link categoryLink = LinkFactory.getCategoryByIDLink(catID);

            Link reviewLink = LinkFactory.getReviewsByProductIDLink(product.getProductId());
            Link inventoryLink = LinkFactory.getInventoryByProductIDLink(product.getProductId());

            Link orderItemLink = LinkFactory.getOrderItemsByProductIDLink(product.getProductId());
            Link wishlistLink = LinkFactory.getWishlistByProductIDLink(product.getProductId());

            Link thumbLink = LinkFactory.getProductThumbnailImage(product.getProductId());
            Link largeLink = LinkFactory.getProductLargeImage(product.getProductId());

            product.add(singleProduct, categoryLink, reviewLink,
                    inventoryLink, orderItemLink, wishlistLink, thumbLink, largeLink);
        }

        Link selfLink = LinkFactory.getProductsBySupplierIDSelfLink(id);
        Link supplierLink = LinkFactory.getSupplierByIDLink(id);
        CollectionModel<Product> results = CollectionModel.of(products, selfLink, supplierLink);

        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/category/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getProductsByCategoryID(@PathVariable Integer id, @PageableDefault(size = 10) Pageable pageable) {
        Page<Product> products = productService.getProductByCategoryID(id, pageable);
        for(final Product product : products) {

            Link singleProduct = LinkFactory.getProductByIDLink(product.getProductId());
            Link reviewLink = LinkFactory.getReviewsByProductIDLink(product.getProductId());
            Link inventoryLink = LinkFactory.getInventoryByProductIDLink(product.getProductId());

            Link orderItemLink = LinkFactory.getOrderItemsByProductIDLink(product.getProductId());
            Link wishlistLink = LinkFactory.getWishlistByProductIDLink(product.getProductId());

            Link thumbLink = LinkFactory.getProductThumbnailImage(product.getProductId());
            Link largeLink = LinkFactory.getProductLargeImage(product.getProductId());

            product.add(singleProduct, reviewLink,
                    inventoryLink, orderItemLink, wishlistLink, thumbLink, largeLink);
        }

        Link selfLink = LinkFactory.getProductsByCategoryIDSelfLink(id);
        Link categoryLink = LinkFactory.getCategoryByIDLink(id);

        CollectionModel<Product> results = CollectionModel.of(products, selfLink, categoryLink);

        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/{id}/thumbs/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getProductThumbImage(@PathVariable Integer id) {
        Optional<Product> product = productService.getProductByID(id);
        Resource resource = resourceLoader.getResource("classpath:/static/assets/images/thumbs/" + id + "/" + product.get().getFeatureImage());
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } else {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @GetMapping(value = "/{id}/large/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getProductLargeImage(@PathVariable Integer id) {
        Optional<Product> product = productService.getProductByID(id);
        Resource resource = resourceLoader.getResource("classpath:/static/assets/images/large/" + id + "/" + product.get().getFeatureImage());
        if (resource.exists() && resource.isReadable()) {

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } else {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @GetMapping(value = "/{productID}/qrcode")
    public ResponseEntity<?> getProductByQRCode(@PathVariable Integer productID) throws Exception {
        Optional<Product> productOptional = productService.getProductByID(productID);
        if(!productOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find product with the id: " + productID);
        }
        Product product = productOptional.get();

        String productLink = LinkFactory.getProductByIDLink(productID).toString();

        BufferedImage discountQRCode = QRFactory.generateQRCodeImage(productLink);
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

        String product_ID = product.getProductId().toString();
        headers.setContentDispositionFormData("filename", product_ID + "product.pdf");

        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
    }

//    @GetMapping(value = "/ratings", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<?> sustainabilityRating() {
//        try{
//            List<Product> products = productService.getAllProducts();
//
//            for (Product product: products) {
//                updateSustainabilityOfProduct(product);
//            }
//            return ResponseEntity.ok("Customers have been updated");
//        } catch (Exception exception){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
//        }
//    }
//
//
//    public void updateSustainabilityOfProduct(Product product){
//        Random random = new Random();
//        // Generate a random number between 1 and 5
//        int randomNumber = random.nextInt(5) + 1;
//        product.setSustainabilityRating(randomNumber);
//        productService.SaveProduct(product);
//    }
}
