package zelora.api.services;

import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zelora.api.model.*;
import zelora.api.repository.OrdersRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private OrdersRepository ordersRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CustomerService customerService;

    public Optional<Orders> getOrderByID(Integer id) {
        return ordersRepository.findById(id);
    }

    public void SaveOrder(Orders orders){
        ordersRepository.save(orders);
    }

    public Page<Orders> findAllWithPagination(Pageable pageable) {
        return ordersRepository.findAll(pageable);
    }

    public Page<Orders> findOrdersByCustomerID(Integer id, Pageable pageable) {
        Optional<Customer> customer = customerService.findOne(id);
        return ordersRepository.findByCustomerId(customer.get(), pageable);
    }

    public BigDecimal calculateDiscountedPrice(Discount discount, Orders order){
        BigDecimal totalPrice = order.getTotalAmount();
        BigDecimal discountValue = discount.getDiscountAmount();

        BigDecimal discountDecimal = discountValue.divide(new BigDecimal(100));
        BigDecimal discountedAmount = totalPrice.multiply(discountDecimal);

        return totalPrice.subtract(discountedAmount);
    }

    public BigDecimal generatingCustomersNewSustainabilityRating(Orders orders, Customer customer) {
        BigDecimal currentSustainabilityRating = customer.getSustainability();
        List<Orderitem> orderContents = orders.getOrderitemList();

        BigDecimal totalSustainabilityRating = BigDecimal.ZERO;
        int itemCount = 0;

        for (Orderitem orderItem : orderContents) {
            Product product = orderItem.getProductId();
            if (product != null && product.getSustainabilityRating() != null) {
                totalSustainabilityRating = totalSustainabilityRating.add(BigDecimal.valueOf(product.getSustainabilityRating()));
                itemCount++;
            }
        }

        BigDecimal averageSustainabilityScore = BigDecimal.ZERO;
        if (itemCount > 0) {
            averageSustainabilityScore = totalSustainabilityRating.divide(BigDecimal.valueOf(itemCount), 2, RoundingMode.HALF_UP);
        }

        BigDecimal newSustainabilityRating = currentSustainabilityRating.add(averageSustainabilityScore);

        return newSustainabilityRating;
    }

    public List<Orderitem> getOrderItemsByOrderID(Integer orderid){
        Optional<Orders> ordersOptional = getOrderByID(orderid);
        if(!ordersOptional.isPresent()){
            return new ArrayList<Orderitem>();
        }
        Orders order = ordersOptional.get();
        return order.getOrderitemList();
    }
}
