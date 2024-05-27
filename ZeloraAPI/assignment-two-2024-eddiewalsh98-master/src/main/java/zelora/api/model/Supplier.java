package zelora.api.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Supplier extends RepresentationModel<Supplier> implements Serializable {


    @Id
    @Basic(optional = false)
    @Column(name = "supplier_id")
    private Integer supplierId;
   
    @Column(name = "supplier_name")
    private String supplierName;
    
    @Column(name = "contact_name")
    private String contactName;
    
    @Column(name = "contact_email")
    private String contactEmail;
    
    @Column(name = "contact_phone")
    private String contactPhone;
    
    @Lob
    @Column(name = "address")
    private String address;
    
    @Column(name = "website")
    private String website;
    
    @Lob
    @Column(name = "description")
    private String description;
    
    @OneToMany(mappedBy = "supplierId")
    @JsonManagedReference
    @ToString.Exclude
    private List<Inventory> inventoryList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplierId")
    @JsonManagedReference
    @ToString.Exclude
    private List<Product> productList;
    
}