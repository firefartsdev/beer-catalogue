package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetBeerUseCase {

    private final BeerRepository beerRepository;

    public Beer getBeer(final UUID beerId) {
        return this.beerRepository.findById(beerId)
                .orElseThrow(() -> new EntityNotFoundException("Beer", beerId));
    }
}
