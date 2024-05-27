package zelora.api.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "orderitems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Orderitem extends RepresentationModel<Orderitem> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "order_item_id")
    private Integer orderItemId;
   
    @Column(name = "quantity")
    private Integer quantity;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "item_price")
    private BigDecimal itemPrice;
   
    @Column(name = "subtotal")
    private BigDecimal subtotal;
   
    @Column(name = "item_weight")
    private BigDecimal itemWeight;
   
    @Lob
    @Column(name = "customisation_options")
    private String customisationOptions;
   
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private Orders orderId;
    
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private Product productId;

}

