package haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer;

import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer.ManufacturerJPAMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BeerRepositoryImpl implements BeerRepository {

    private final BeerJPARepository beerJPARepository;

    private final BeerJPAMapper beerJPAMapper;
    private final ManufacturerJPAMapper manufacturerJPAMapper;

    @Override
    public List<Beer> findAll() {
        final var beers = beerJPARepository.findAll();
        return this.beerJPAMapper.toDomainList(beers);
    }

    @Override
    public Optional<Beer> findById(UUID beerId) {
        final var beer = beerJPARepository.findById(beerId);
        return beer
                .map(this.beerJPAMapper::toDomain);
    }

    @Override
    public Beer create(Beer beer) {
        final var newBeer = this.beerJPARepository.save(this.beerJPAMapper.toJpa(beer));
        return this.beerJPAMapper.toDomain(newBeer);
    }

    @Override
    public Beer update(UUID beerId, Beer beer) {
        final var manufacturer = this.manufacturerJPAMapper.toJpa(beer.manufacturer());
        final var beerToUpdate = new BeerJPAEntity(beerId, beer.name(), beer.abv(), beer.type(), beer.description(), manufacturer);
        final var updatedBeer = this.beerJPARepository.save(beerToUpdate);
        return this.beerJPAMapper.toDomain(updatedBeer);
    }

    @Override
    public void delete(UUID beerId) {
        this.beerJPARepository.deleteById(beerId);
    }
}
