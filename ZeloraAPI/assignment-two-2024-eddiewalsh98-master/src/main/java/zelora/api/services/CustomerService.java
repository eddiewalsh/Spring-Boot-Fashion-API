package zelora.api.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zelora.api.model.Customer;
import zelora.api.repository.CustomerRepository;
import java.util.List;

import java.util.Optional;

@Service
public class CustomerService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CustomerRepository customerRepo;

    public void saveCustomer(Customer a) {
        customerRepo.save(a);
    }

    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }

    public void updateCustomer(Customer editedCustomer) {
        if(customerRepo.findById(editedCustomer.getCustomerId()).isPresent()) {
            saveCustomer(editedCustomer);
        }
    }

    public boolean archiveCustomer(Integer custID) {
        Optional<Customer> customer = customerRepo.findById(custID);
        if(customer.isPresent()) {
            customer.get().setArchived(true);
            saveCustomer(customer.get());
            return true;
        } else {
            return false;
        }
    }

    public Optional<Customer> findOne(Integer id) {
        return customerRepo.findById(id);
    }

    public Page<Customer> findAllWithPagination(Pageable pageable) {
        return customerRepo.getCustomersByArchived(false, pageable);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepo.getCustomerByEmail(email);
    }

    public boolean isCustomerValid(String email) {
        Optional<Customer> customer = getCustomerByEmail(email);
        if(customer.isPresent()){
            return true;
        } else{
            return false;
        }
    }

}
