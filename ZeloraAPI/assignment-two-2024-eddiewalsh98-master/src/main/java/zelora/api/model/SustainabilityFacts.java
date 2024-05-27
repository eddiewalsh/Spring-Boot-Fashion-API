package zelora.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sustainability_facts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SustainabilityFacts extends RepresentationModel<SustainabilityFacts> implements Serializable {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sustainable_id")
    private Integer sustainable_id;

    @Column(name = "fact")
    private String fact;

    @Column(name = "source")
    private String source;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "accessed_on")
    private Date accessedOn;
}
