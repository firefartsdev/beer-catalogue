package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.infrastructure.adapter.s3.S3Repository;
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

    @Mock
    private S3Repository s3Repository;

    @InjectMocks
    private GetBeerUseCase getBeerUseCase;

    @Test
    void shouldReturnBeerWhenExists() {
        UUID beerId = UUID.randomUUID();
        Manufacturer manufacturer = new Manufacturer(UUID.randomUUID(), "Brew Co", "Spain");
        Beer beer = new Beer(beerId, "Test", 5.0, "Ale", "Test desc", manufacturer, "imageUrl", null);
        Beer expectedBeer = new Beer(beerId, "Test", 5.0, "Ale", "Test desc", manufacturer, "imageUrl", "imageBase64");

        when(beerRepository.findById(beerId)).thenReturn(Optional.of(beer));
        when(s3Repository.download(beer.imageUrl())).thenReturn("imageBase64");

        Beer result = getBeerUseCase.getBeer(beerId);

        assertEquals(expectedBeer, result);
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