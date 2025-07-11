package haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "manufacturers")
public class ManufacturerJPAEntity {

    public ManufacturerJPAEntity() {
    }

    public ManufacturerJPAEntity(UUID id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
