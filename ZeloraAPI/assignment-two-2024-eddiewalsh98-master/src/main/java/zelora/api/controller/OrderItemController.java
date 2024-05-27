package zelora.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zelora.api.helper.LinkFactory;
import zelora.api.model.Customer;
import zelora.api.model.Orderitem;
import zelora.api.services.OrderItemService;
import zelora.api.services.OrdersService;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/zelora/order/items")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrdersService ordersService;


    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getOrderItemByID(@PathVariable Integer id) {
        Optional<Orderitem> orderItem = orderItemService.getOrderItemByID(id);

        if(!orderItem.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Could not find item with the item " + id);
        }

        Integer productID = orderItem.get().getProductId().getProductId();
        Integer orderID = orderItem.get().getOrderId().getOrderId();
        Integer customerID = ordersService.getOrderByID(orderID).get()
                                        .getCustomerId().getCustomerId();

        Link selfLink = LinkFactory.getOrderItemByIDSelfLink(id);
        Link orderLink = LinkFactory.getOrderItemByOrderIDLink(orderID);
        Link productLink = LinkFactory.getProductByIDLink(productID);
        Link customerLink = LinkFactory.getCustomerByIDLink(customerID);

        EntityModel<Orderitem> result = EntityModel.of(orderItem.get(),
                                selfLink, orderLink, productLink, customerLink);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/product/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getOrderItemByProductID(@PathVariable Integer id, @PageableDefault(size = 10)Pageable pageable) {
        Page<Orderitem> orderitems = orderItemService.getOrderItemsByProductID(id, pageable);
        for (final Orderitem item: orderitems) {
            Link orderItemLink = LinkFactory.getOrderItemByIDLink(item.getOrderItemId());
            item.add(orderItemLink);
        }

        Link selfLink = LinkFactory.getOrderItemsByProductIDSelfLink(id);
        Link productLink = LinkFactory.getProductByIDLink(id);

        CollectionModel<Orderitem> results = CollectionModel.of(orderitems, selfLink, productLink);
        return ResponseEntity.ok(results);
    }
}
