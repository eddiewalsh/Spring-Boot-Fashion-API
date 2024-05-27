package zelora.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zelora.api.model.Supplier;
import zelora.api.repository.SupplierRepository;

import java.util.Optional;

@Service
public class SupplierService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private SupplierRepository supplierRepository;

    public Optional<Supplier> getSupplierByID(Integer id) {
        return supplierRepository.findById(id);
    }
}
