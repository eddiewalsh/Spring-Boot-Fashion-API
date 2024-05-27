package zelora.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "customerdiscount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Discount extends RepresentationModel<Discount> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "discount_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer discountId;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "message")
    private String message;

    @Column(name = "redeemed")
    private boolean redeemed;

    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private Customer customerId;
}
