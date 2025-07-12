package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer.ManufacturerRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateBeerUseCase {

    private final BeerRepository beerRepository;
    private final ManufacturerRepository manufacturerRepository;

    public Beer updateBeer(final UUID beerId, final Beer beer) {
        final var beerToUpdate = this.beerRepository.findById(beerId)
                .orElseThrow(() -> new EntityNotFoundException("Beer", beerId));
        if(!beerToUpdate.manufacturer().id().equals(beer.manufacturer().id())) {
            this.manufacturerRepository.findById(beer.manufacturer().id())
                    .orElseThrow(() -> new EntityNotFoundException("Manufacturer", beer.manufacturer().id()));
        }
        return this.beerRepository.update(beerToUpdate.id(), beer);
    }
}
