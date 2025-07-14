package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
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
class CreateBeerUseCaseTest {

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private CreateBeerUseCase createBeerUseCase;

    @Test
    void shouldCreateBeerWhenManufacturerExists() {
        // Arrange
        UUID manufacturerId = UUID.randomUUID();
        UUID beerId = UUID.randomUUID();
        Manufacturer manufacturer = new Manufacturer(manufacturerId, "Brew Co.", "Spain");
        Beer inputBeer = new Beer(beerId, "Test Beer", 5.0, "IPA", "Nice IPA", manufacturer, null, null);

        when(manufacturerRepository.findById(manufacturerId)).thenReturn(Optional.of(manufacturer));
        when(beerRepository.create(any(Beer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Beer result = createBeerUseCase.createBeer(inputBeer, null);

        // Assert
        assertNotNull(result);
        assertEquals("Test Beer", result.name());
        assertEquals(5.0, result.abv());
        assertEquals("IPA", result.type());
        assertEquals("Nice IPA", result.description());
        assertEquals(manufacturer, result.manufacturer());

        verify(manufacturerRepository).findById(manufacturerId);
        verify(beerRepository).create(any(Beer.class));
    }

    @Test
    void shouldThrowExceptionWhenManufacturerNotFound() {
        // Arrange
        UUID manufacturerId = UUID.randomUUID();
        UUID beerId = UUID.randomUUID();
        Manufacturer manufacturer = new Manufacturer(manufacturerId, "Unknown", "Unknown");
        Beer inputBeer = new Beer(beerId, "Ghost Beer", 6.0, "Lager", "No brewer", manufacturer, null, null);

        when(manufacturerRepository.findById(manufacturerId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                createBeerUseCase.createBeer(inputBeer, null)
        );

        assertEquals("Manufacturer with id %s not found".formatted(manufacturerId), ex.getMessage());

        verify(manufacturerRepository).findById(manufacturerId);
        verify(beerRepository, never()).create(any());
    }
}