package haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer;

import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSearchCriteria;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSort;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.SortDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class BeerRepositoryImplTest {

    @Mock
    private BeerJPARepository beerJPARepository;

    @Mock
    private BeerJPAMapper beerJPAMapper;

    @InjectMocks
    private BeerRepositoryImpl beerRepository;

    private UUID beerId;
    private Beer beer;
    private BeerJPAEntity beerEntity;

    @BeforeEach
    void setUp() {
        beerId = UUID.randomUUID();
        beer = mock(Beer.class);
        beerEntity = mock(BeerJPAEntity.class);
    }

    @Test
    void findAll_returnsMappedBeers() {
        BeerSort sort = new BeerSort(SortDirection.ASC.toString(), "name");
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        List<BeerJPAEntity> entities = List.of(beerEntity);
        Page<BeerJPAEntity> entityPage = new PageImpl<>(entities);

        when(beerJPARepository.findAll(pageable)).thenReturn(entityPage);
        when(beerJPAMapper.toDomain(beerEntity)).thenReturn(beer);

        Page<Beer> result = beerRepository.findAll(sort, 0, 10);

        assertThat(result.getContent()).containsExactly(beer);
        verify(beerJPARepository).findAll(pageable);
    }

    @Test
    void search_returnsMappedBeers() {
        BeerSearchCriteria criteria = new BeerSearchCriteria("Lager", null, null, null);
        BeerSort sort = new BeerSort(SortDirection.DESC.toString(), "abv");
        PageRequest pageable = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "abv"));
        Specification<BeerJPAEntity> spec = mock(Specification.class);
        Page<BeerJPAEntity> entityPage = new PageImpl<>(List.of(beerEntity));

        when(beerJPAMapper.fromCriteria(criteria)).thenReturn(spec);
        when(beerJPARepository.findAll(spec, pageable)).thenReturn(entityPage);
        when(beerJPAMapper.toDomain(beerEntity)).thenReturn(beer);

        Page<Beer> result = beerRepository.search(criteria, sort, 1, 5);

        assertThat(result.getContent()).containsExactly(beer);
    }

    @Test
    void findById_returnsMappedBeer() {
        when(beerJPARepository.findById(beerId)).thenReturn(Optional.of(beerEntity));
        when(beerJPAMapper.toDomain(beerEntity)).thenReturn(beer);

        Optional<Beer> result = beerRepository.findById(beerId);

        assertThat(result).contains(beer);
    }

    @Test
    void create_savesAndReturnsMappedBeer() {
        when(beerJPAMapper.toJpa(beer)).thenReturn(beerEntity);
        when(beerJPARepository.save(beerEntity)).thenReturn(beerEntity);
        when(beerJPAMapper.toDomain(beerEntity)).thenReturn(beer);

        Beer result = beerRepository.create(beer);

        assertThat(result).isEqualTo(beer);
    }

    @Test
    void update_savesAndReturnsMappedBeer() {
        when(beerJPAMapper.toJpa(beer)).thenReturn(beerEntity);
        when(beerJPARepository.save(beerEntity)).thenReturn(beerEntity);
        when(beerJPAMapper.toDomain(beerEntity)).thenReturn(beer);

        Beer result = beerRepository.update(beerId, beer);

        assertThat(result).isEqualTo(beer);
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        beerRepository.delete(beerId);

        verify(beerJPARepository).deleteById(beerId);
    }
}