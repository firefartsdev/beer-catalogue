package haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "manufacturers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManufacturerJPAEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

}
