package haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer;

import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSearchCriteria;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer.spec.BeerSpecification.*;

@Mapper(componentModel = "spring")
public interface BeerJPAMapper {

    Beer toDomain(BeerJPAEntity entity);

    List<Beer> toDomainList(List<BeerJPAEntity> entities);

    BeerJPAEntity toJpa(Beer beer);

    default Specification<BeerJPAEntity> fromCriteria(BeerSearchCriteria criteria) {
        Specification<BeerJPAEntity> spec = null;

        if(criteria.name() != null && !criteria.name().isBlank()) {
            spec = nameContains(criteria.name());
        }

        if(criteria.type() != null && !criteria.type().isBlank()) {
            spec = (spec == null) ? typeEquals(criteria.type()) : spec.and(typeEquals(criteria.type()));
        }

        if(criteria.abv() != null) {
            spec = (spec == null) ? abvEquals(criteria.abv()) : spec.and(abvEquals(criteria.abv()));
        }

        if(criteria.manufacturerName() != null && !criteria.manufacturerName().isBlank()) {
            spec = (spec == null) ? manufacturerNameContains(criteria.manufacturerName()) : spec.and(manufacturerNameContains(criteria.manufacturerName()));
        }

        return spec;
    }
}
