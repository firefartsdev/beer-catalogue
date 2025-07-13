package haufe.group.beer_catalogue.application.manufacturer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.application.manufacturer.usecase.UpdateManufacturerUseCase;
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
class UpdateManufacturerUseCaseTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private UpdateManufacturerUseCase useCase;

    @Test
    void shouldUpdateManufacturerWhenExists() {
        UUID id = UUID.randomUUID();
        Manufacturer existing = new Manufacturer(id, "Old Name", "Spain");
        Manufacturer updated = new Manufacturer(id, "New Name", "Spain");

        when(manufacturerRepository.findById(id)).thenReturn(Optional.of(existing));
        when(manufacturerRepository.update(id, updated)).thenReturn(updated);

        Manufacturer result = useCase.updateManufacturer(id, updated);

        assertEquals(updated, result);
        verify(manufacturerRepository).update(id, updated);
    }

    @Test
    void shouldThrowIfManufacturerDoesNotExist() {
        UUID id = UUID.randomUUID();
        Manufacturer updated = new Manufacturer(id, "New Name", "New Country");

        when(manufacturerRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                useCase.updateManufacturer(id, updated)
        );

        assertEquals("Manufacturer with id %s not found".formatted(id), ex.getMessage());
    }
}