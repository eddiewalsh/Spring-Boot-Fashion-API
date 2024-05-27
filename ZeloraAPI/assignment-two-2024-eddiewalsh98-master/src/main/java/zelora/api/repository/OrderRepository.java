package zelora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
}
