package zelora.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zelora.api.model.Customer;
import zelora.api.model.Orderitem;
import zelora.api.model.Orders;
import zelora.api.model.Product;
import zelora.api.repository.OrderItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrdersService orderService;

    @Autowired
    private ProductService productService;

    public Optional<Orderitem> getOrderItemByID(Integer id) {
        return orderItemRepository.findById(id);
    }

    public Page<Orderitem> findAllWithPagination(Pageable pageable) {
        return orderItemRepository.findAll(pageable);
    }

    public Page<Orderitem> getOrderItemsByOrderID(Integer orderID, Pageable pageable) {
        Optional<Orders> order = orderService.getOrderByID(orderID);
        return orderItemRepository.findByOrderId(order.get(), pageable);
    }

    public Page<Orderitem> getOrderItemsByProductID(Integer productID, Pageable pageable) {
        Optional<Product> product = productService.getProductByID(productID);
        return orderItemRepository.findByProductId(product.get(), pageable);
    }
}
