package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteBeerUseCaseTest {

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private DeleteBeerUseCase deleteBeerUseCase;

    @Test
    void shouldDeleteBeerById() {
        UUID beerId = UUID.randomUUID();

        deleteBeerUseCase.deleteBeer(beerId);

        verify(beerRepository).delete(beerId);
    }
}