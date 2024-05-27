package zelora.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;


@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Inventory extends RepresentationModel<Inventory> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "inventory_id")
    private Integer inventoryId;
   
    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;
   
    @Column(name = "quantity_reserved")
    private Integer quantityReserved;
   
    @Column(name = "reorder_point")
    private Integer reorderPoint;
   
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private Product productId;
    
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private Supplier supplierId;

       
}