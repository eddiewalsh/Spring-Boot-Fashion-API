package zelora.api.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "orders")
public class Orders extends RepresentationModel<Orders> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "order_id")
    private Integer orderId;
    
    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    
    @Column(name = "order_status")
    private String orderStatus;
    
    @Column(name = "payment_method")
    private String paymentMethod;
    
    @Column(name = "shipping_method")
    private String shippingMethod;
    
    @Column(name = "delivery_date")
    @Temporal(TemporalType.DATE)
    private Date deliveryDate;

    @Column(name = "discount_used")
    private Boolean discountUsed;
    
    @OneToMany(mappedBy = "orderId")
    @JsonManagedReference(value = "orderitem-orders")
    @ToString.Exclude
    private List<Orderitem> orderitemList;
    
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private Customer customerId;

    @JoinColumn(name = "discount_id", referencedColumnName = "discount_id")
    @OneToOne
    @JsonBackReference
    @ToString.Exclude
    private Discount discount;
}
  


