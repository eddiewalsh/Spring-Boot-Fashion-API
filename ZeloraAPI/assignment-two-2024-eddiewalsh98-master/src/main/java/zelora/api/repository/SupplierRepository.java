package zelora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
