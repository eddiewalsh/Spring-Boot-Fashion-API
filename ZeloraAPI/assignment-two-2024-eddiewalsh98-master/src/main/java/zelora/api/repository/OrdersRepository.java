package zelora.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Customer;
import zelora.api.model.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    public Page<Orders> findByCustomerId(Customer customer, Pageable pageable);
}
