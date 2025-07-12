package haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer;

import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BeerJPAMapper {

    Beer toDomain(BeerJPAEntity entity);

    List<Beer> toDomainList(List<BeerJPAEntity> entities);

    BeerJPAEntity toJpa(Beer beer);
}
