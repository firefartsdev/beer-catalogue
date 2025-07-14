package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSort;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListBeersUseCaseTest {

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private ListBeersUseCase listBeersUseCase;

    @Test
    void shouldReturnPaginatedAndSortedBeers() {
        // Given
        BeerSort sort = new BeerSort("ASC", "abv");
        int page = 1;
        int size = 2;

        Manufacturer manufacturer = mock(Manufacturer.class);
        Beer beer1 = new Beer(UUID.randomUUID(), "Beer 1", 5.0, "IPA", "description", manufacturer, null, null);
        Beer beer2 = new Beer(UUID.randomUUID(), "Beer 2", 6.0, "IPA", "description", manufacturer, null, null);
        List<Beer> content = List.of(beer1, beer2);
        Page<Beer> expectedPage = new PageImpl<>(content, PageRequest.of(page, size), 10);

        Mockito.when(beerRepository.findAll(sort, page, size)).thenReturn(expectedPage);

        // When
        Page<Beer> result = listBeersUseCase.listBeers(sort, page, size);

        // Then
        assertEquals(2, result.getContent().size());
        assertEquals("Beer 1", result.getContent().get(0).name());
        verify(beerRepository).findAll(sort, page, size);
    }
}