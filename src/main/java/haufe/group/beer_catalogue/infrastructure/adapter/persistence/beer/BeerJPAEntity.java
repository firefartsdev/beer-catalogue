package haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer;

import haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer.ManufacturerJPAEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "beers")
public class BeerJPAEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double abv;

    @Column(nullable = false)
    private String type;

    @Column(length = 1000)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private ManufacturerJPAEntity manufacturer;

    // Constructors

    public BeerJPAEntity() {
    }

    public BeerJPAEntity(UUID id, String name, double abv, String type, String description, ManufacturerJPAEntity manufacturer) {
        this.id = id;
        this.name = name;
        this.abv = abv;
        this.type = type;
        this.description = description;
        this.manufacturer = manufacturer;
    }
}
