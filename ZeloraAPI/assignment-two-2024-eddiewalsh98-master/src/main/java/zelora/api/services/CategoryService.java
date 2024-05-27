package zelora.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zelora.api.model.Category;
import zelora.api.repository.CategoryRepository;

import java.util.Optional;

@Service
public class CategoryService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CategoryRepository categoryRepository;

    public Optional<Category> getCategoryByID(Integer id) {
        return categoryRepository.findById(id);
    }
}
