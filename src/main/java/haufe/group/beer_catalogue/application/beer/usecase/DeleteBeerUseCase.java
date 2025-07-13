package haufe.group.beer_catalogue.application.beer.usecase;

import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteBeerUseCase {

    private final BeerRepository beerRepository;

    public void deleteBeer(final UUID beerId) {
        this.beerRepository.delete(beerId);
    }
}
