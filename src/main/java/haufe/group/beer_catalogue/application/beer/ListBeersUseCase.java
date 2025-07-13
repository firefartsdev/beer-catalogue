package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListBeersUseCase {

    private final BeerRepository beerRepository;

    public List<Beer> listBeers(BeerSort sort) {
        return this.beerRepository.findAll(sort);
    }
}
