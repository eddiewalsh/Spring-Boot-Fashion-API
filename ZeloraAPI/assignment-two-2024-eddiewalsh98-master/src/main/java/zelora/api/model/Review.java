package zelora.api.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;


@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Review extends RepresentationModel<Review> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "review_id")
    private Integer reviewId;
    
    @Column(name = "rating")
    private Integer rating;
    
    @Lob
    @Column(name = "review_text")
    private String reviewText;
    
    @Column(name = "review_date")
    @Temporal(TemporalType.DATE)
    private Date reviewDate;
    
    @Column(name = "flagged_as_spam")
    private Boolean flaggedAsSpam;
    
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private Customer customerId;
    
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private Product productId;

}