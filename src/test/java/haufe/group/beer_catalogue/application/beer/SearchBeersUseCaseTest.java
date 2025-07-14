package haufe.group.beer_catalogue.application.beer;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSearchCriteria;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSort;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchBeersUseCaseTest {

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private SearchBeersUseCase searchBeersUseCase;

    @Test
    void shouldDelegateSearchToRepository() {
        // given
        BeerSearchCriteria criteria = new BeerSearchCriteria("lager", "ale", 5.0, "heineken");
        BeerSort sort = new BeerSort("ASC", "name");
        int page = 0;
        int size = 10;

        Manufacturer mockedManufacturer = mock(Manufacturer.class);
        Beer dummyBeer = new Beer(UUID.randomUUID(), "Sample Beer", 5.0, "ale", "A description", mockedManufacturer, null, null);
        Page<Beer> dummyResult = new PageImpl<>(List.of(dummyBeer));

        when(beerRepository.search(criteria, sort, page, size)).thenReturn(dummyResult);

        // when
        Page<Beer> result = searchBeersUseCase.searchBeers(criteria, sort, page, size);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Sample Beer");

        verify(beerRepository).search(criteria, sort, page, size);
    }
}