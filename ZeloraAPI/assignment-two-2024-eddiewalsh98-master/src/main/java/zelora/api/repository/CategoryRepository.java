package zelora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zelora.api.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
