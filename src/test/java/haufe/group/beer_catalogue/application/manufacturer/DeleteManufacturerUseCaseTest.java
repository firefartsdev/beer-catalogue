package haufe.group.beer_catalogue.application.manufacturer;

import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteManufacturerUseCaseTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private DeleteManufacturerUseCase useCase;

    @Test
    void shouldDeleteManufacturerById() {
        UUID id = UUID.randomUUID();
        useCase.deleteManufacturer(id);
        verify(manufacturerRepository).delete(id);
    }
}