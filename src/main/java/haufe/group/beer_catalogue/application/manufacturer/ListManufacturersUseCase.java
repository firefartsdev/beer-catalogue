package haufe.group.beer_catalogue.application.manufacturer;

import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListManufacturersUseCase {

    private final ManufacturerRepository manufacturerRepository;

    public ListManufacturersUseCase(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public List<Manufacturer> listManufacturers() {
        return manufacturerRepository.findAll();
    }
}
