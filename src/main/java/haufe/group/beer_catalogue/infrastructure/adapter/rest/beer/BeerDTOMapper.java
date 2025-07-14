package haufe.group.beer_catalogue.infrastructure.adapter.rest.beer;

import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSearchCriteria;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto.BeerDTO;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto.BeerSearchRequestDTO;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto.CreateBeerRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BeerDTOMapper {

    Beer toDomain(BeerDTO dto);
    List<Beer> toDomainList(List<BeerDTO> dtos);

    @Mapping(target = "manufacturer", expression = "java(new Manufacturer(dto.getManufacturerId(), null, null))")
    Beer toDomainFromCreate(CreateBeerRequestDTO dto);

    BeerDTO toDTO(Beer beer);
    List<BeerDTO> toDTOList(List<Beer> beers);

    BeerSearchCriteria toCriteria(BeerSearchRequestDTO dto);
}
