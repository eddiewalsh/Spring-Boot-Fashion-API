package zelora.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zelora.api.helper.LinkFactory;
import zelora.api.model.*;
import zelora.api.security.JwtUtil;
import zelora.api.services.CustomerService;
import zelora.api.services.DiscountService;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/zelora/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @RequestMapping(method = RequestMethod.PUT, value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        try
        {
            customerService.updateCustomer(customer);

            Link selfLink = linkTo(methodOn(CustomerController.class)
                    .updateCustomer(customer)).withSelfRel();

            Link customerLink = LinkFactory.getCustomerByIDLink(customer.getCustomerId());

            Link allCustomerLink = LinkFactory.getAllCustomersLink();

            EntityModel<Customer> result = EntityModel.of(customer, selfLink, customerLink, allCustomerLink);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error");
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/remove/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> removeCustomer(@PathVariable Integer id) {
        if(customerService.archiveCustomer(id)) {

            Link selfLink = linkTo(methodOn(CustomerController.class)
                    .removeCustomer(id)).withSelfRel();

            Link customerLink = LinkFactory.getCustomerByIDLink(id);

            Link allCustomers = LinkFactory.getAllCustomersLink();

            EntityModel<String> results = EntityModel.of("Customer Successfully removed"
                    ,selfLink, customerLink, allCustomers);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(results);

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find customer with the ID " + id);
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public ResponseEntity<?> addCustomer(@RequestBody Customer request) {

        if(customerService.getCustomerByEmail(request.getEmail()).isPresent()) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Customer already exists with this email");
        }

        customerService.saveCustomer(request);
        Link selfLink = linkTo(methodOn(CustomerController.class)
                .addCustomer(request)).withSelfRel();

        Link customerLink = LinkFactory.getCustomerByEmailLink(request.getEmail());

        Link allCustomers = LinkFactory.getAllCustomersLink();

        EntityModel<Customer> results = EntityModel.of(request, selfLink, customerLink, allCustomers);

        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllCustomers(@PageableDefault(size = 10)Pageable pageable) {
        Page<Customer> customers = customerService.findAllWithPagination(pageable);

        for(final Customer customer : customers) {
            Integer id = customer.getCustomerId();
            Link customerLink = LinkFactory.getCustomerByIDLink(id);
            Link customerOrdersLink = LinkFactory.getOrdersByCustomerIDLink(id);
            Link reviewsLink = LinkFactory.getReviewsByCustomerIDLink(id);
            Link wishlistLink = LinkFactory.getWishlistByCustomerIDLink(id);
            Link discountLink = LinkFactory.getDiscountsByCustomerIDLink(id);
            customer.add(customerLink, customerOrdersLink, reviewsLink, wishlistLink, discountLink);
        }

        Link selfLink = LinkFactory.getAllCustomersSelfLink();
        CollectionModel<Customer> results = CollectionModel.of(customers, selfLink);

        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getCustomerByID(@PathVariable Integer id){
        Optional<Customer> customer = customerService.findOne(id);

        if(!customer.isPresent() || customer.get().isArchived()) {
            EntityModel<String> responseModel = EntityModel.of("Could not find customer with the id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
        }

        Link selfLink = LinkFactory.getCustomerByIDSelfLink(id);
        Link customerOrdersLink = LinkFactory.getOrdersByCustomerIDLink(id);
        Link reviewsLink = LinkFactory.getReviewsByCustomerIDLink(id);
        Link wishlistLink = LinkFactory.getWishlistByCustomerIDLink(id);
        Link discountLink = LinkFactory.getDiscountsByCustomerIDLink(id);
        Link allCustomerLink = LinkFactory.getAllCustomersLink();

        EntityModel<Customer> result = EntityModel.of(customer.get(), selfLink,
                                                customerOrdersLink, reviewsLink,
                                                wishlistLink, discountLink,
                                                allCustomerLink);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/email/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getCustomerByEmail(@PathVariable String email) {

        Optional<Customer> customer = customerService.getCustomerByEmail(email);

        if(!customer.isPresent() || customer.get().isArchived()) {
            EntityModel<String> responseModel = EntityModel.of("Could not find customer with the email " + email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
        }

        Integer customerID = customer.get().getCustomerId();

        Link selfLink = LinkFactory.getCustomerByEmailSelfLink(email);
        Link customerOrdersLink = LinkFactory.getOrdersByCustomerIDLink(customer.get().getCustomerId());
        Link reviewsLink = LinkFactory.getReviewsByCustomerIDLink(customerID);
        Link wishlistLink = LinkFactory.getWishlistByCustomerIDLink(customerID);
        Link discountsLink = LinkFactory.getDiscountsByCustomerIDLink(customerID);

        Link allCustomerLink = LinkFactory.getAllCustomersLink();

        EntityModel<Customer> result = EntityModel.of(customer.get(), selfLink, customerOrdersLink,
                                                    reviewsLink, wishlistLink, discountsLink,
                                                    allCustomerLink);

        return ResponseEntity.ok(result);
    }
}