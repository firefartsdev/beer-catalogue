package haufe.group.beer_catalogue.application.manufacturer;

import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateManufacturerUseCase {

    private final ManufacturerRepository manufacturerRepository;

    public CreateManufacturerUseCase(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public Manufacturer createManufacturer(final Manufacturer manufacturer) {
        return this.manufacturerRepository.create(manufacturer);
    }
}
