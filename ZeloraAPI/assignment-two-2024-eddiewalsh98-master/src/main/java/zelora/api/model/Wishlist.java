package zelora.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "wishlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Wishlist extends RepresentationModel<Wishlist> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "wishlist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wishlistId;

    @Column(name = "added_date")
    @Temporal(TemporalType.DATE)
    private Date addedDate;

    @Column(name = "wishlist_name")
    private String wishlistName;

    @Lob
    @Column(name = "notes")
    private String notes;

    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne
    @JsonBackReference(value = "customer-wishlist")
    @ToString.Exclude
    private Customer customerId;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne
    @JsonBackReference(value = "product-wishlist")
    @ToString.Exclude
    private Product productId;
}
