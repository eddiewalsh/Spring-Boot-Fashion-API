package zelora.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Customer;
import zelora.api.model.Product;
import zelora.api.model.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    public Page<Wishlist> findByCustomerId(Customer customer, Pageable pageable);
    public Page<Wishlist> findByProductId(Product product, Pageable pageable);
}
