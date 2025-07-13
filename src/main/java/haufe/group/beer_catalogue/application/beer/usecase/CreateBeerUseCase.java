package haufe.group.beer_catalogue.application.beer.usecase;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateBeerUseCase {

    private final BeerRepository beerRepository;
    private final ManufacturerRepository manufacturerRepository;

    public Beer createBeer(Beer beer) {
        final var manufacturer = this.manufacturerRepository.findById(beer.manufacturer().id())
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer", beer.manufacturer().id()));
        final var newBeer = new Beer(
                beer.id(),
                beer.name(),
                beer.abv(),
                beer.type(),
                beer.description(),
                manufacturer
        );
        return this.beerRepository.create(newBeer);
    }
}
