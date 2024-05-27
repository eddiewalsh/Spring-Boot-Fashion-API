package zelora.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import zelora.api.model.Supplier;
import zelora.api.services.SupplierService;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/zelora/")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping(value = "/supplier/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getSupplierByID(@PathVariable Integer id) {
        Optional<Supplier> supplier = supplierService.getSupplierByID(id);

        if(!supplier.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Could not find supplier with the id " + id);
        }

        Link selfLink = LinkFactory.getSupplierByIDSelfLink(id);
        Link inventoryLink = LinkFactory.getInventoryBySupplierIDLink(id);
        Link productLink = LinkFactory.getProductsBySupplierIDLink(id);


        EntityModel<Supplier> results = EntityModel.of(supplier.get(), selfLink,
                                                    inventoryLink, productLink);

        return ResponseEntity.ok(results);
    }
}
