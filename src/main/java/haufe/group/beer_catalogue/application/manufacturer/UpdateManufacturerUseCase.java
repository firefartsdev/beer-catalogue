package haufe.group.beer_catalogue.application.manufacturer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateManufacturerUseCase {

    private final ManufacturerRepository manufacturerRepository;

    public Manufacturer updateManufacturer(final UUID manufacturerId, final Manufacturer manufacturer) {
        final var manufacturerToUpdate = this.manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer", manufacturerId));
        return this.manufacturerRepository.update(manufacturerToUpdate.id(), manufacturer);
    }
}