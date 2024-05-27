package zelora.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Orderitem;
import zelora.api.model.Orders;
import zelora.api.model.Product;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<Orderitem, Integer> {
    public Page<Orderitem> findByOrderId(Orders order, Pageable pageable);
    public Page<Orderitem> findByProductId(Product product, Pageable pageable);
}
