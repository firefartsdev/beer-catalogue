package haufe.group.beer_catalogue.application.manufacturer.usecase;

import haufe.group.beer_catalogue.application.manufacturer.ManufacturerSort;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListManufacturersUseCase {

    private final ManufacturerRepository manufacturerRepository;

    public List<Manufacturer> listManufacturers(ManufacturerSort sort) {
        return manufacturerRepository.findAll(sort);
    }
}
