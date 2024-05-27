package zelora.api.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;


@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Category extends RepresentationModel<Category> implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "category_id")
    private Integer categoryId;
    
    @Column(name = "category_name")
    private String categoryName;
   
    @Column(name = "category_image")
    private String categoryImage;
    
    @OneToMany(mappedBy = "categoryId")
    @JsonManagedReference
    @ToString.Exclude
    private List<Product> productList;   

}