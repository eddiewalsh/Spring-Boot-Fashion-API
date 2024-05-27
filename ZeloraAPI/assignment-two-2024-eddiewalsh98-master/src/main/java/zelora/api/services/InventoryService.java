package zelora.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import zelora.api.model.Inventory;
import zelora.api.model.Product;
import zelora.api.model.Supplier;
import zelora.api.repository.InventoryRepository;

import java.util.Optional;

@Service
public class InventoryService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private SupplierService supplierService;

    public Optional<Inventory> getInventoryByID(Integer id){
        return inventoryRepository.findById(id);
    }

    public Page<Inventory> getInventoryByProductID(Integer id, Pageable pageable){
        Optional<Product> product = productService.getProductByID(id);
        return inventoryRepository.findByProductId(product.get(), pageable);
    }

    public Page<Inventory> getInventoryBySupplierID(Integer id, Pageable pageable){
        Optional<Supplier> supplier = supplierService.getSupplierByID(id);
        return inventoryRepository.findBySupplierId(supplier.get(), pageable);
    }
}
