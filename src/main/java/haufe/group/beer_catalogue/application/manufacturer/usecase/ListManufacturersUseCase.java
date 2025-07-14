package haufe.group.beer_catalogue.application.manufacturer.usecase;

import haufe.group.beer_catalogue.application.manufacturer.ManufacturerSort;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ListManufacturersUseCase {

    private final ManufacturerRepository manufacturerRepository;

    public Page<Manufacturer> listManufacturers(final ManufacturerSort sort, final int page, final int size) {
        return manufacturerRepository.findAll(sort, page, size);
    }
}
