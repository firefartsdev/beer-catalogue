package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.domain.beer.vo.BeerSort;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ListBeersUseCase {

    private final BeerRepository beerRepository;

    public Page<Beer> listBeers(final BeerSort sort, final int page, final int size) {
        return this.beerRepository.findAll(sort, page, size);
    }
}
