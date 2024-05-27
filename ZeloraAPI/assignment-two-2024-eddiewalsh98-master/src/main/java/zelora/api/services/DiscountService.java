package zelora.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zelora.api.helper.ProjectHelper;
import zelora.api.model.Customer;
import zelora.api.model.Discount;
import zelora.api.model.Orders;
import zelora.api.repository.DiscountRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class DiscountService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    DiscountRepository discountRepository;

    public void SaveDiscount(Discount discount) {
        discountRepository.save(discount);
    }

    public Page<Discount> getDiscountsByCustomer(Customer customer, Pageable pageable) {
        return discountRepository.findDiscountByCustomerId(customer, pageable);
    }

    public Optional<Discount> getDiscountByID(Integer id){
        return discountRepository.findById(id);
    }

    public void checkForExpirationDate(Discount discount) {
        Date today = ProjectHelper.getTodaysDate();
        if(discount.getExpirationDate().after(today)) {
            discount.setRedeemed(true);
            SaveDiscount(discount);
        }
    }

    public Discount GenerateBirthdayDiscount(Customer customer) {
        Discount discount = new Discount();
        discount.setDiscountAmount(BigDecimal.valueOf(10.00));
        discount.setMessage("Happy Birthday " + customer.getFirstName() + " !");
        discount.setExpirationDate(ProjectHelper.calculateExpirationDate());
        discount.setCustomerId(customer);
        discountRepository.save(discount);

        return  discount;
    }

    public Discount GenerateFirstPurchaseDiscount(Customer customer) {
        Discount discount = new Discount();
        discount.setDiscountAmount(BigDecimal.valueOf(5.00));
        discount.setMessage("Thank you for your first purchase...Next one is on us !");
        discount.setExpirationDate(ProjectHelper.calculateExpirationDate());
        discount.setCustomerId(customer);
        SaveDiscount(discount);

        return discount;
    }

    public Discount GenerateTenthPurchaseDiscount(Customer customer) {
        Discount discount = new Discount();
        discount.setDiscountAmount(BigDecimal.valueOf(10.00));
        discount.setMessage("Wow, 10 Orders? You must really like us!");
        discount.setExpirationDate(ProjectHelper.calculateExpirationDate());
        discount.setCustomerId(customer);
        SaveDiscount(discount);

        return discount;
    }

    public Discount GenerateSustainabilityDiscount(Customer customer) {
        Discount discount = new Discount();
        discount.setDiscountAmount(BigDecimal.valueOf(25.00));
        discount.setMessage("On behalf of the planet, thank you :)");
        discount.setExpirationDate(ProjectHelper.calculateExpirationDate());
        discount.setCustomerId(customer);
        discountRepository.save(discount);

        return discount;
    }
}
