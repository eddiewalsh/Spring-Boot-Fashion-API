package zelora.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zelora.api.model.Category;
import zelora.api.model.Customer;
import zelora.api.model.Product;
import zelora.api.model.Supplier;
import zelora.api.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SupplierService supplierService;


    public Optional<Product> getProductByID(Integer id){
        return productRepository.findById(id);
    }

    public Page<Product> findAllWithPagination(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductBySupplierID(Integer id, Pageable pageable){
        Optional<Supplier> supplier = supplierService.getSupplierByID(id);
        return productRepository.findBySupplierId(supplier.get(), pageable);
    }

    public Page<Product> getProductByCategoryID(Integer id, Pageable pageable){
        Optional<Category> category = categoryService.getCategoryByID(id);
        return productRepository.findByCategoryId(category.get(), pageable);
    }

    public List<Product> getAllProducts(){ return productRepository.findAll(); }

    public void SaveProduct(Product product){
        productRepository.save(product);
    }
}
