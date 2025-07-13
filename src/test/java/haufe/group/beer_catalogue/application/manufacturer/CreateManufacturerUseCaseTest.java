package haufe.group.beer_catalogue.application.manufacturer;

import haufe.group.beer_catalogue.application.manufacturer.usecase.CreateManufacturerUseCase;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateManufacturerUseCaseTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private CreateManufacturerUseCase useCase;

    @Test
    void shouldCreateManufacturer() {
        Manufacturer manufacturer = new Manufacturer(UUID.randomUUID(), "Brew Co", "Spain");
        when(manufacturerRepository.create(manufacturer)).thenReturn(manufacturer);

        Manufacturer result = useCase.createManufacturer(manufacturer);

        assertEquals(manufacturer, result);
        verify(manufacturerRepository).create(manufacturer);
    }
}