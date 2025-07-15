package haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.vo.ManufacturerSort;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.SortDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ManufacturerRepositoryImplTest {

    @Mock
    private ManufacturerJPARepository manufacturerJPARepository;

    @Mock
    private ManufacturerJPAMapper manufacturerJPAMapper;

    @InjectMocks
    private ManufacturerRepositoryImpl manufacturerRepository;

    private UUID manufacturerId;
    private Manufacturer manufacturer;
    private ManufacturerJPAEntity manufacturerEntity;

    @BeforeEach
    void setUp() {
        manufacturerId = UUID.randomUUID();
        manufacturer = mock(Manufacturer.class);
        manufacturerEntity = mock(ManufacturerJPAEntity.class);
    }

    @Test
    void findAll_returnsMappedManufacturers() {
        ManufacturerSort sort = new ManufacturerSort(SortDirection.ASC.toString(), "name");
        PageRequest pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "name"));
        Page<ManufacturerJPAEntity> entityPage = new PageImpl<>(List.of(manufacturerEntity));

        when(manufacturerJPARepository.findAll(pageable)).thenReturn(entityPage);
        when(manufacturerJPAMapper.toDomain(manufacturerEntity)).thenReturn(manufacturer);

        Page<Manufacturer> result = manufacturerRepository.findAll(sort, 0, 20);

        assertThat(result.getContent()).containsExactly(manufacturer);
    }

    @Test
    void findById_returnsMappedManufacturer() {
        when(manufacturerJPARepository.findById(manufacturerId)).thenReturn(Optional.of(manufacturerEntity));
        when(manufacturerJPAMapper.toDomain(manufacturerEntity)).thenReturn(manufacturer);

        Optional<Manufacturer> result = manufacturerRepository.findById(manufacturerId);

        assertThat(result).contains(manufacturer);
    }

    @Test
    void create_savesAndReturnsMappedManufacturer() {
        when(manufacturerJPAMapper.toJpa(manufacturer)).thenReturn(manufacturerEntity);
        when(manufacturerJPARepository.save(manufacturerEntity)).thenReturn(manufacturerEntity);
        when(manufacturerJPAMapper.toDomain(manufacturerEntity)).thenReturn(manufacturer);

        Manufacturer result = manufacturerRepository.create(manufacturer);

        assertThat(result).isEqualTo(manufacturer);
    }

    @Test
    void update_savesAndReturnsMappedManufacturer() {
        ManufacturerJPAEntity updatedEntity = new ManufacturerJPAEntity(manufacturerId, manufacturer.name(), manufacturer.country());

        when(manufacturer.name()).thenReturn("TestName");
        when(manufacturer.country()).thenReturn("TestCountry");
        when(manufacturerJPARepository.save(any())).thenReturn(updatedEntity);
        when(manufacturerJPAMapper.toDomain(updatedEntity)).thenReturn(manufacturer);

        Manufacturer result = manufacturerRepository.update(manufacturerId, manufacturer);

        assertThat(result).isEqualTo(manufacturer);
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        manufacturerRepository.delete(manufacturerId);

        verify(manufacturerJPARepository).deleteById(manufacturerId);
    }
}