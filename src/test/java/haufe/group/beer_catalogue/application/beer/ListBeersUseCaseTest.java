package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.application.beer.usecase.ListBeersUseCase;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListBeersUseCaseTest {

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private ListBeersUseCase listBeersUseCase;

    @Test
    void shouldListBeersSorted() {
        BeerSort sort = new BeerSort("ASC", "name");
        List<Beer> beers = List.of(mock(Beer.class));

        when(beerRepository.findAll(sort)).thenReturn(beers);

        List<Beer> result = listBeersUseCase.listBeers(sort);

        assertEquals(beers, result);
        verify(beerRepository).findAll(sort);
    }
}