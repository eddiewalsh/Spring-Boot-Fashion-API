package zelora.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Customer;
import zelora.api.model.Orders;
import zelora.api.model.Product;
import zelora.api.model.Review;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Review, Integer> {

    public Page<Review> findByCustomerId(Customer customer, Pageable pageable);

    public Page<Review> findByProductId(Product product, Pageable pageable);
}
