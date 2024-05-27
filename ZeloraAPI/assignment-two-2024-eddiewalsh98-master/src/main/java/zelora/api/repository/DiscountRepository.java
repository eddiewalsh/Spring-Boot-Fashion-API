package zelora.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Customer;
import zelora.api.model.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    public Page<Discount> findDiscountByCustomerId(Customer customer, Pageable pageable);

}
