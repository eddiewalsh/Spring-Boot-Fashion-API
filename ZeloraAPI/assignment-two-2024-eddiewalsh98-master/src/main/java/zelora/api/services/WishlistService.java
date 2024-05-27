package zelora.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zelora.api.model.Customer;
import zelora.api.model.Product;
import zelora.api.model.Wishlist;
import zelora.api.repository.WishlistRepository;

import java.util.Optional;

@Service
public class WishlistService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;

    public Optional<Wishlist> getWishlistByID(Integer id) {
        return wishlistRepository.findById(id);
    }

    public Page<Wishlist> findAllWithPagination(Pageable pageable) {
        return wishlistRepository.findAll(pageable);
    }

    public Page<Wishlist> getWishlistByCustomerID(Integer id, Pageable pageable){
        Optional<Customer> customer = customerService.findOne(id);
        return wishlistRepository.findByCustomerId(customer.get(), pageable);
    }

    public Page<Wishlist> getWishlistByProductID(Integer id, Pageable pageable){
        Optional<Product> product = productService.getProductByID(id);
        return wishlistRepository.findByProductId(product.get(), pageable);
    }

    public Wishlist SaveWishlist(Wishlist wishlist){
        wishlistRepository.save(wishlist);
        return wishlist;
    }

    public boolean RemoveWishlist(Wishlist wishlist){
        try{
            wishlistRepository.delete(wishlist);
            return true;
        } catch (Exception exception){
            return false;
        }
    }
}
