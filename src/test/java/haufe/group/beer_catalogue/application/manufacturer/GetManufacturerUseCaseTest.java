package haufe.group.beer_catalogue.application.manufacturer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetManufacturerUseCaseTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private GetManufacturerUseCase useCase;

    @Test
    void shouldReturnManufacturerIfExists() {
        UUID id = UUID.randomUUID();
        Manufacturer manufacturer = new Manufacturer(id, "Brew Co", "Spain");

        when(manufacturerRepository.findById(id)).thenReturn(Optional.of(manufacturer));

        Manufacturer result = useCase.getManufacturer(id);

        assertEquals(manufacturer, result);
    }

    @Test
    void shouldThrowIfManufacturerDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(manufacturerRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                useCase.getManufacturer(id)
        );

        assertEquals("Manufacturer with id %s not found".formatted(id), ex.getMessage());
    }
}