package zelora.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zelora.api.helper.LinkFactory;
import zelora.api.model.Customer;
import zelora.api.model.Product;
import zelora.api.model.Wishlist;
import zelora.api.services.CustomerService;
import zelora.api.services.ProductService;
import zelora.api.services.WishlistService;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/zelora/wishlist")
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getWishlistByID(@PathVariable Integer id){
        Optional<Wishlist> wishlist = wishlistService.getWishlistByID(id);
        if(!wishlist.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Could not find wishlist with the id " + id);
        }
        Integer productID = wishlist.get().getProductId().getProductId();
        Integer customerID = wishlist.get().getCustomerId().getCustomerId();

        Link selfLink = LinkFactory.getWishlistByIDSelfLink(id);
        Link productLink = LinkFactory.getProductByIDLink(productID);
        Link customerLink = LinkFactory.getCustomerByIDSelfLink(customerID);

        EntityModel<Wishlist> results = EntityModel.of(wishlist.get(), selfLink,
                                                        productLink, customerLink);

        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/customers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getWishlistsByCustomerID(@PathVariable Integer id, @PageableDefault(size = 10) Pageable pageable) {
        Page<Wishlist> wishlists = wishlistService.getWishlistByCustomerID(id, pageable);

        for (final Wishlist wishlist: wishlists) {
            Integer wishlistID = wishlist.getWishlistId();
            Integer productID = wishlist.getProductId().getProductId();
            Link wishlistLink = LinkFactory.getWishlistByIDLink(wishlistID);
            Link productLink = LinkFactory.getProductByIDLink(productID);

            wishlist.add(wishlistLink,productLink);
        }

        Link selfLink = LinkFactory.getWishlistByIDSelfLink(id);
        Link customerLink = LinkFactory.getCustomerByIDLink(id);

        CollectionModel<Wishlist> results = CollectionModel.of(wishlists,
                                                            selfLink, customerLink);

        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/product/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getWishlistByProductID(@PathVariable Integer id, @PageableDefault(size = 10)Pageable pageable) {
        Page<Wishlist> wishlists = wishlistService.getWishlistByProductID(id, pageable);
        for (final Wishlist list: wishlists) {
            Link wishlistLink = LinkFactory.getWishlistByIDLink(list.getWishlistId());
            list.add(wishlistLink);
        }

        Link selfLink = LinkFactory.getWishlistByProductIDSelfLink(id);
        Link productLink = LinkFactory.getProductByIDLink(id);

        CollectionModel<Wishlist> results = CollectionModel.of(wishlists, selfLink, productLink);
        return ResponseEntity.ok(results);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> addWishlist(@RequestBody Wishlist wishlist) {

        if(wishlist.getCustomerId() == null || wishlist.getProductId() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find customer or product");
        }

        wishlist.setProductId(productService.getProductByID(wishlist.getProductId().getProductId()).get());
        wishlist.setCustomerId(customerService.getCustomerByEmail(wishlist.getCustomerId().getEmail()).get());

        Wishlist newWishlist = wishlistService.SaveWishlist(wishlist);

        Link selfLink = linkTo(methodOn(WishlistController.class).addWishlist(wishlist)).withSelfRel();
        Link wishlistLink = LinkFactory.getWishlistByIDLink(newWishlist.getWishlistId());
        Link customerLink = LinkFactory.getCustomerByEmailLink(newWishlist.getCustomerId().getEmail());

        EntityModel<Wishlist> results = EntityModel.of(newWishlist, selfLink, wishlistLink, customerLink);

        return ResponseEntity.ok(results);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> updateWishlist(@RequestBody Wishlist wishlist) {

        if(wishlist.getCustomerId() == null || wishlist.getProductId() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find customer or product");
        }

        wishlist.setProductId(productService.getProductByID(wishlist.getProductId().getProductId()).get());
        wishlist.setCustomerId(customerService.getCustomerByEmail(wishlist.getCustomerId().getEmail()).get());

        wishlistService.SaveWishlist(wishlist);

        Link selfLink = linkTo(methodOn(WishlistController.class).addWishlist(wishlist)).withSelfRel();
        Link wishlistLink = LinkFactory.getWishlistByIDLink(wishlist.getWishlistId());
        Link customerLink = LinkFactory.getCustomerByEmailLink(wishlist.getCustomerId().getEmail());

        EntityModel<Wishlist> results = EntityModel.of(wishlist, selfLink, wishlistLink, customerLink);

        return ResponseEntity.ok(results);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/remove", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> removeWishlist(@RequestBody Wishlist wishlist) {

        Customer customer = customerService.getCustomerByEmail(wishlist.getCustomerId().getEmail()).get();

        wishlist.setProductId(productService.getProductByID(wishlist.getProductId().getProductId()).get());
        wishlist.setCustomerId(customer);

       if(!wishlistService.RemoveWishlist(wishlist)){
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed removing wishlist");
       }


        Link selfLink = linkTo(methodOn(WishlistController.class).removeWishlist(wishlist)).withSelfRel();
        Link customerLink = LinkFactory.getCustomerByEmailLink(customer.getEmail());

        EntityModel<Link> results = EntityModel.of(selfLink, customerLink);

        return ResponseEntity.ok(results);
    }
}
