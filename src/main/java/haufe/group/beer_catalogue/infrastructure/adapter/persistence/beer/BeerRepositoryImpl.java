package haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer;

import haufe.group.beer_catalogue.domain.beer.vo.BeerSearchCriteria;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSort;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer.spec.BeerSpecification;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer.ManufacturerJPAMapper;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.SortDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BeerRepositoryImpl implements BeerRepository {

    private final BeerJPARepository beerJPARepository;
    private final BeerJPAMapper beerJPAMapper;

    @Override
    public Page<Beer> findAll(final BeerSort sort, final int page, final int size) {
        Sort.Direction direction = sort.getDirection().equals(SortDirection.ASC.toString()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort jpsSort = Sort.by(direction, sort.getSortBy());
        final var pageable = PageRequest.of(page, size, jpsSort);

        final var beers = beerJPARepository.findAll(pageable);
        return beers.map(this.beerJPAMapper::toDomain);
    }

    @Override
    public Page<Beer> search(final BeerSearchCriteria criteria, final BeerSort sort, final int page, final int size) {
        Specification<BeerJPAEntity> spec = this.beerJPAMapper.fromCriteria(criteria);
        Sort.Direction direction = sort.getDirection().equals(SortDirection.ASC.toString()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort jpsSort = Sort.by(direction, sort.getSortBy());
        final var pageable = PageRequest.of(page, size, jpsSort);

        return beerJPARepository.findAll(spec, pageable)
                .map(this.beerJPAMapper::toDomain);
    }

    @Override
    public Optional<Beer> findById(final UUID beerId) {
        final var beer = beerJPARepository.findById(beerId);
        return beer
                .map(this.beerJPAMapper::toDomain);
    }

    @Override
    public Beer create(final Beer beer) {
        final var newBeer = this.beerJPARepository.save(this.beerJPAMapper.toJpa(beer));
        return this.beerJPAMapper.toDomain(newBeer);
    }

    @Override
    public Beer update(final UUID beerId, final Beer beer) {
        final var beerToUpdate = this.beerJPAMapper.toJpa(beer);
        final var updatedBeer = this.beerJPARepository.save(beerToUpdate);
        return this.beerJPAMapper.toDomain(updatedBeer);
    }

    @Override
    public void delete(final UUID beerId) {
        this.beerJPARepository.deleteById(beerId);
    }
}
