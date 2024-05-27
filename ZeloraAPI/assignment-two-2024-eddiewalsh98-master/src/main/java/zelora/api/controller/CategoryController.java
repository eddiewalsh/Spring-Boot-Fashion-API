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
import zelora.api.model.Category;
import zelora.api.services.CategoryService;

import java.security.Principal;
import java.util.Optional;


@RestController
@RequestMapping("/zelora/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getCategoryByID(@PathVariable Integer id) {
        Optional<Category> category = categoryService.getCategoryByID(id);
        if(!category.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find category with the id " + id);
        }

        Link selfLink = LinkFactory.getCategoryByIDSelfLink(id);
        Link productLink = LinkFactory.getProductsByCategoryIDLink(id);

        EntityModel<Category> results = EntityModel.of(category.get(), selfLink, productLink);

        return ResponseEntity.ok(results);
    }
}
