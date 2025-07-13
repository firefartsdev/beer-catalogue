package haufe.group.beer_catalogue.domain.beer.port;

import haufe.group.beer_catalogue.application.beer.BeerSort;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerRepository {

    List<Beer> findAll(BeerSort sort);

    Optional<Beer> findById(UUID beerId);

    Beer create(Beer beer);

    Beer update(UUID beerId, Beer beer);

    void delete(UUID beerId);
}
