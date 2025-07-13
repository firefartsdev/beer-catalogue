package haufe.group.beer_catalogue.application.manufacturer.usecase;

import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateManufacturerUseCase {

    private final ManufacturerRepository manufacturerRepository;

    public Manufacturer createManufacturer(final Manufacturer manufacturer) {
        return this.manufacturerRepository.create(manufacturer);
    }
}
