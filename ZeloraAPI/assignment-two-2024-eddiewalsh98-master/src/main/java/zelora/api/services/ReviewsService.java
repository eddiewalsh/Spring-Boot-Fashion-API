package zelora.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zelora.api.model.Customer;
import zelora.api.model.Product;
import zelora.api.model.Review;
import zelora.api.repository.ReviewsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewsService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ReviewsRepository reviewsRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;

    public Optional<Review> getReviewByID(Integer Id){
        return reviewsRepository.findById(Id);
    }

    public Page<Review> findAllWithPagination(Pageable pageable) {
        return reviewsRepository.findAll(pageable);
    }

    public Page<Review> getReviewsByCustomerID(Integer id, Pageable pageable) {
        Optional<Customer> customer = customerService.findOne(id);
        return reviewsRepository.findByCustomerId(customer.get(), pageable);
    }

    public Page<Review> getReviewsByProductID(Integer id, Pageable pageable) {
        Optional<Product> product = productService.getProductByID(id);
        return reviewsRepository.findByProductId(product.get(), pageable);
    }
}
