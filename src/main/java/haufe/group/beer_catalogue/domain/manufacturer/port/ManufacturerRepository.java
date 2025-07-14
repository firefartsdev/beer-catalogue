package haufe.group.beer_catalogue.domain.manufacturer.port;

import haufe.group.beer_catalogue.application.manufacturer.ManufacturerSort;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManufacturerRepository {

    Page<Manufacturer> findAll(ManufacturerSort sort, int page, int size);

    Optional<Manufacturer> findById(UUID manufacturerId);

    Manufacturer create(Manufacturer manufacturer);

    Manufacturer update(UUID manufacturerId, Manufacturer manufacturer);

    void delete(UUID manufacturerId);
}
