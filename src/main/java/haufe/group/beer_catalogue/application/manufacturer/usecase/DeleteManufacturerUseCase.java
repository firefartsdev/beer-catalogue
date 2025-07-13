package haufe.group.beer_catalogue.application.manufacturer.usecase;

import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteManufacturerUseCase {

    private final ManufacturerRepository manufacturerRepository;

    public void deleteManufacturer(final UUID manufacturerId) {
        this.manufacturerRepository.delete(manufacturerId);
    }
}
