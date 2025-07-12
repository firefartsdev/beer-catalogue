package haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerJPARepository extends JpaRepository<BeerJPAEntity, UUID> {
}
