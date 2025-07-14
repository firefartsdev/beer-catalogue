package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSearchCriteria;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchBeersUseCase {

    private final BeerRepository beerRepository;

    public Page<Beer> searchBeers(final BeerSearchCriteria criteria, final BeerSort sort, final int page, final int size) {
        return this.beerRepository.search(criteria, sort, page, size);
    }
}
