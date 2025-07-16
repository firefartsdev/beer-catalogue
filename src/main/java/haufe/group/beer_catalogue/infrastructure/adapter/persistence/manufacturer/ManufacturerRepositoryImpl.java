package haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer;

import haufe.group.beer_catalogue.domain.manufacturer.vo.ManufacturerSort;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.SortDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ManufacturerRepositoryImpl implements ManufacturerRepository {

    private final ManufacturerJPARepository manufacturerJPARepository;

    private final ManufacturerJPAMapper manufacturerJPAMapper;

    @Override
    public Page<Manufacturer> findAll(final ManufacturerSort sort, final int page, final int size) {
        Sort.Direction direction = sort.getDirection().equals(SortDirection.ASC.toString()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort jpsSort = Sort.by(direction, sort.getSortBy());
        final var pageable = PageRequest.of(page, size, jpsSort);

        final var manufacturers = this.manufacturerJPARepository.findAll(pageable);
        return manufacturers.map(this.manufacturerJPAMapper::toDomain);
    }

    @Override
    public Optional<Manufacturer> findById(final UUID manufacturerId) {
        final var manufacturer = this.manufacturerJPARepository.findById(manufacturerId);
        return manufacturer
                .map(this.manufacturerJPAMapper::toDomain);
    }

    @Override
    public Manufacturer create(final Manufacturer manufacturer) {
        final var newManufacturer = this.manufacturerJPARepository.save(this.manufacturerJPAMapper.toJpa(manufacturer));
        return this.manufacturerJPAMapper.toDomain(newManufacturer);
    }

    @Override
    public Manufacturer update(final UUID manufacturerId, final Manufacturer manufacturer) {
        final var manufacturerToUpdate = new ManufacturerJPAEntity(manufacturerId, manufacturer.name(), manufacturer.country());
        final var updatedManufacturer = this.manufacturerJPARepository.save(manufacturerToUpdate);
        return this.manufacturerJPAMapper.toDomain(updatedManufacturer);
    }

    @Override
    public void delete(final UUID manufacturerId) {
        this.manufacturerJPARepository.deleteById(manufacturerId);
    }
}
