package haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer;

import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ManufacturerRepositoryImpl implements ManufacturerRepository {

    private final ManufacturerJPARepository manufacturerJPARepository;

    private final ManufacturerJPAMapper manufacturerJPAMapper;

    @Override
    public List<Manufacturer> findAll() {
        final var manufacturers = this.manufacturerJPARepository.findAll();
        return this.manufacturerJPAMapper.toDomainList(manufacturers);
    }

    @Override
    public Optional<Manufacturer> findById(UUID manufacturerId) {
        final var manufacturer = this.manufacturerJPARepository.findById(manufacturerId);
        return manufacturer
                .map(this.manufacturerJPAMapper::toDomain);
    }

    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        final var newManufacturer = this.manufacturerJPARepository.save(this.manufacturerJPAMapper.toJpa(manufacturer));
        return this.manufacturerJPAMapper.toDomain(newManufacturer);
    }

    @Override
    public Manufacturer update(UUID manufacturerId, Manufacturer manufacturer) {
        final var manufacturerToUpdate = new ManufacturerJPAEntity(manufacturerId, manufacturer.name(), manufacturer.country());
        final var updatedManufacturer = this.manufacturerJPARepository.save(manufacturerToUpdate);
        return this.manufacturerJPAMapper.toDomain(updatedManufacturer);
    }

    @Override
    public void delete(UUID manufacturerId) {
        this.manufacturerJPARepository.deleteById(manufacturerId);
    }
}
