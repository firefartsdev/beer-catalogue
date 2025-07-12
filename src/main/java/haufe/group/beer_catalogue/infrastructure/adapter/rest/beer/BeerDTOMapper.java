package haufe.group.beer_catalogue.infrastructure.adapter.rest.beer;

import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BeerDTOMapper {

    Beer toDomain(BeerDTO dto);
    List<Beer> toDomainList(List<BeerDTO> dtos);

    @Mapping(target = "manufacturer", expression = "java(new Manufacturer(dto.manufacturerId(), null, null))")
    Beer toDomainFromCreate(CreateBeerRequestDTO dto);

    BeerDTO toDTO(Beer beer);
    List<BeerDTO> toDTOList(List<Beer> beers);
}
