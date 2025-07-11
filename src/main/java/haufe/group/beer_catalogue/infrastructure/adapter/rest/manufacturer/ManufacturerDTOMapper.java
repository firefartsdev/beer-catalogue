package haufe.group.beer_catalogue.infrastructure.adapter.rest.manufacturer;

import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ManufacturerDTOMapper {

    ManufacturerDTO toDTO(Manufacturer manufacturer);

    List<ManufacturerDTO> toDTOList(List<Manufacturer> manufacturers);

    Manufacturer toDomain(ManufacturerDTO manufacturerDTO);

    List<Manufacturer> toDomainList(List<ManufacturerDTO> manufacturerDTOList);
}
