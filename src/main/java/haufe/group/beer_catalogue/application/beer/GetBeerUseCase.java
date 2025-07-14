package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.infrastructure.adapter.s3.S3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetBeerUseCase {

    private final BeerRepository beerRepository;
    private final S3Repository s3Repository;

    public Beer getBeer(final UUID beerId) {
        final var beer = beerRepository.findById(beerId);
        return beer.map(b -> {
            final var image = s3Repository.download(b.imageUrl());
            return new Beer(b.id(), b.name(), b.abv(), b.type(), b.description(), b.manufacturer(), b.imageUrl(), image);
        }).orElseThrow(() -> new EntityNotFoundException("Beer", beerId));
    }
}
