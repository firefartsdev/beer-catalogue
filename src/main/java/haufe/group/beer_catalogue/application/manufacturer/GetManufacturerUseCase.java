package haufe.group.beer_catalogue.application.manufacturer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetManufacturerUseCase {

    private final ManufacturerRepository manufacturerRepository;

    public Manufacturer getManufacturer(final UUID manufacturerId) {
        return this.manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer", manufacturerId));
    }
}
