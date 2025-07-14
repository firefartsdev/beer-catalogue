package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
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
class GetBeerUseCaseTest {

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private GetBeerUseCase getBeerUseCase;

    @Test
    void shouldReturnBeerWhenExists() {
        UUID beerId = UUID.randomUUID();
        Manufacturer manufacturer = new Manufacturer(UUID.randomUUID(), "Brew Co", "Spain");
        Beer beer = new Beer(beerId, "Test", 5.0, "Ale", "Test desc", manufacturer, null, null);

        when(beerRepository.findById(beerId)).thenReturn(Optional.of(beer));

        Beer result = getBeerUseCase.getBeer(beerId);

        assertEquals(beer, result);
    }

    @Test
    void shouldThrowExceptionWhenBeerNotFound() {
        UUID beerId = UUID.randomUUID();
        when(beerRepository.findById(beerId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                getBeerUseCase.getBeer(beerId)
        );

        assertEquals("Beer with id %s not found".formatted(beerId), exception.getMessage());
    }
}