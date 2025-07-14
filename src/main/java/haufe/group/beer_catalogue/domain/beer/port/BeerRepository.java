package haufe.group.beer_catalogue.domain.beer.port;

import haufe.group.beer_catalogue.domain.beer.vo.BeerSearchCriteria;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSort;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface BeerRepository {

    Page<Beer> findAll(BeerSort sort, int page, int size);

    Page<Beer> search(BeerSearchCriteria criteria, BeerSort sort, int page, int size);

    Optional<Beer> findById(UUID beerId);

    Beer create(Beer beer);

    Beer update(UUID beerId, Beer beer);

    void delete(UUID beerId);
}
