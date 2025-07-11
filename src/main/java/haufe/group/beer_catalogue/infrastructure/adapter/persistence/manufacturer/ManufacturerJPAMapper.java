package haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer;

import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ManufacturerJPAMapper {

    Manufacturer toDomain(ManufacturerJPAEntity entity);

    List<Manufacturer> toDomainList(List<ManufacturerJPAEntity> entities);

    ManufacturerJPAEntity toJpa(Manufacturer domain);
}
