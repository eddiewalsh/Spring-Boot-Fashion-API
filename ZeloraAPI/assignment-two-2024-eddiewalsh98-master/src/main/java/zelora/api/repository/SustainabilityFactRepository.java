package zelora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Supplier;
import zelora.api.model.SustainabilityFacts;

@Repository
public interface SustainabilityFactRepository extends JpaRepository<SustainabilityFacts, Integer> {
}
