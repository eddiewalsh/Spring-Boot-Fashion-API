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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zelora.api.helper.LinkFactory;
import zelora.api.model.Inventory;
import zelora.api.model.Product;
import zelora.api.model.Review;
import zelora.api.model.Supplier;
import zelora.api.services.InventoryService;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/zelora/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getInventoryByID(@PathVariable Integer id){
        Optional<Inventory> inventory = inventoryService.getInventoryByID(id);

        if(!inventory.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Could not find inventory with the id " + id);
        }

        Integer productId = inventory.get().getProductId().getProductId();
        Integer supplierId = inventory.get().getSupplierId().getSupplierId();

        Link selfLink = LinkFactory.getInventoryByIDSelfLink(id);
        Link productLink = LinkFactory.getProductByIDLink(productId);
        Link supplierLink = LinkFactory.getSupplierByIDLink(supplierId);

        EntityModel<Inventory> results = EntityModel.of(inventory.get(), selfLink, productLink, supplierLink);
        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/products/{productID}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getInventoryByProductID(@PathVariable Integer productID, @PageableDefault(size = 10) Pageable pageable) {
        Page<Inventory> inventories = inventoryService.getInventoryByProductID(productID, pageable);

        for (Inventory inventory: inventories) {
            Integer supplierID = inventory.getSupplierId().getSupplierId();
            Link inventoryLink = LinkFactory.getInventoryByIDLink(inventory.getInventoryId());
            Link supplierLink = LinkFactory.getSupplierByIDLink(supplierID);
            inventory.add(inventoryLink, supplierLink);
        }

        Link selfLink = LinkFactory.getInventoryByProductIDSelfLink(productID);
        Link productLink = LinkFactory.getProductByIDLink(productID);

        CollectionModel<Inventory> result = CollectionModel.of(inventories, selfLink, productLink);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/supplier/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getInventoryBySupplierID(@PathVariable Integer id, @PageableDefault(size = 10)Pageable pageable) {
        Page<Inventory> inventories = inventoryService.getInventoryBySupplierID(id, pageable);
        for (final Inventory inventory : inventories) {
            Integer productId = inventory.getProductId().getProductId();
            Link inventoryLink = LinkFactory.getInventoryByIDLink(inventory.getInventoryId());
            Link productLink = LinkFactory.getProductByIDLink(productId);

            inventory.add(inventoryLink, productLink);
        }

        Link selfLink = LinkFactory.getInventoryBySupplierIDSelfLink(id);

        CollectionModel<Inventory> results = CollectionModel.of(inventories, selfLink);

        return ResponseEntity.ok(results);
    }
}
