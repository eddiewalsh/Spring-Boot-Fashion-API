package zelora.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Category;
import zelora.api.model.Product;
import zelora.api.model.Supplier;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    public Page<Product> findBySupplierId(Supplier supplier, Pageable pageable);
    public Page<Product> findByCategoryId(Category category, Pageable pageable);
}
