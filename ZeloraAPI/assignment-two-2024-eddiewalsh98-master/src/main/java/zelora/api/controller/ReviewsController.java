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
import zelora.api.model.Review;
import zelora.api.services.ReviewsService;
import java.util.Optional;


@RestController
@RequestMapping("/zelora/reviews")
public class ReviewsController {
    @Autowired
    private ReviewsService reviewsService;

    @GetMapping(value ="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getReviewByID(@PathVariable("id") Integer id){
        Optional<Review> review = reviewsService.getReviewByID(id);
        if(!review.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find review with the ID " + id);
        }
        Integer customerID = review.get().getCustomerId().getCustomerId();
        Integer productID = review.get().getProductId().getProductId();

        Link selfLink = LinkFactory.getReviewByIDSelfLink(id);
        Link customerLink = LinkFactory.getCustomerByIDLink(customerID);
        Link productLink = LinkFactory.getProductByIDLink(productID);
        Link allReviews = LinkFactory.getAllReviewsLink(PageRequest.of(0, 10));

        EntityModel<Review> result = EntityModel.of(review.get(), selfLink, customerLink, productLink, allReviews);

        return ResponseEntity.ok(result);
    }


    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAllReviews(Pageable pageable) {
        Page<Review> reviews = reviewsService.findAllWithPagination(pageable);
        for(final Review review : reviews) {
            Integer id = review.getReviewId();
            Integer customerID = review.getCustomerId().getCustomerId();
            Integer productID = review.getProductId().getProductId();

            Link reviewLink = LinkFactory.getReviewByIDLink(id);
            Link customerLink = LinkFactory.getCustomerByIDLink(customerID);
            Link productLink = LinkFactory.getProductByIDLink(productID);

            review.add(reviewLink,customerLink, productLink);
        }

        Link selfLink = LinkFactory.getAllReviewsSelfLink(pageable);
        CollectionModel<Review> result = CollectionModel.of(reviews, selfLink);

        return ResponseEntity.ok(result);
    }


    @GetMapping(value = "/customers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getReviewsByCustomerID(@PathVariable Integer id,@PageableDefault(size = 10)Pageable pageable){
        Page<Review> reviews = reviewsService.getReviewsByCustomerID(id, pageable);

        for (final Review review: reviews) {
            Integer reviewId = review.getReviewId();
            Integer productid = review.getProductId().getProductId();

            Link reviewLink = LinkFactory.getReviewByIDLink(reviewId);
            Link productLink = LinkFactory.getProductByIDLink(productid);
            review.add(reviewLink, productLink);
        }

        Link selfLink = LinkFactory.getReviewsByCustomerIDSelfLink(id, pageable);
        Link customerLink = LinkFactory.getCustomerByIDLink(id);

        CollectionModel<Review> results = CollectionModel.of(reviews, selfLink, customerLink);

        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/products/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getReviewsByProductID(@PathVariable Integer id,@PageableDefault(size = 10)Pageable pageable){
        Page<Review> reviews = reviewsService.getReviewsByProductID(id, pageable);

        for (final Review review: reviews) {
            Integer reviewId = review.getReviewId();
            Integer customerID = review.getCustomerId().getCustomerId();

            Link reviewLink = LinkFactory.getReviewByIDLink(reviewId);
            Link customerLink = LinkFactory.getCustomerByIDLink(customerID);
            review.add(reviewLink, customerLink);
        }

        Link selfLink = LinkFactory.getReviewsByProductIDSelfLink(id);
        Link productLink = LinkFactory.getProductByIDLink(id);

        CollectionModel<Review> results = CollectionModel.of(reviews, selfLink, productLink);

        return ResponseEntity.ok(results);
    }
}
