package zelora.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Inventory;
import zelora.api.model.Product;
import zelora.api.model.Supplier;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    public Page<Inventory> findByProductId(Product product, Pageable pageable);
    public Page<Inventory> findBySupplierId(Supplier supplier, Pageable pageable);
}
