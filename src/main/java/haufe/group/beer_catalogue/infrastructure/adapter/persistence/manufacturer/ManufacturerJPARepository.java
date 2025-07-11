package haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManufacturerJPARepository extends JpaRepository<ManufacturerJPAEntity, UUID> {
}
