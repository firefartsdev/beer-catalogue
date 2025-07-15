package haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer;

import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSearchCriteria;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSort;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer.ManufacturerJPAEntity;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer.ManufacturerJPARepository;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.SortDirection;
import haufe.group.beer_catalogue.infrastructure.adapter.s3.S3Repository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test-nos3")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerRepositoryIT {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @MockitoBean
    private S3Repository s3Repository;

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private ManufacturerJPARepository manufacturerRepository;

    @BeforeEach
    void setup() {
        manufacturerRepository.deleteAll();
    }

    @Transactional
    @Test
    @Order(1)
    void create_and_findById_shouldWork() {
        var savedManufacturer = manufacturerRepository.save(new ManufacturerJPAEntity(null, "Mahou", "Spain"));
        var manufacturer = new Manufacturer(savedManufacturer.getId(), savedManufacturer.getName(), savedManufacturer.getCountry());

        var beer = new Beer(null, "Mahou 5 Estrellas", 5.5, "Lager", "Popular en España", manufacturer, "imageUrl", null);

        var savedBeer = beerRepository.create(beer);

        assertThat(savedBeer.id()).isNotNull();
        assertThat(beerRepository.findById(savedBeer.id())).isPresent();
    }

    @Transactional
    @Test
    @Order(2)
    void findAll_shouldReturnSorted() {
        var savedManufacturer = manufacturerRepository.save(new ManufacturerJPAEntity(null, "Mahou", "Spain"));
        var manufacturer = new Manufacturer(savedManufacturer.getId(), savedManufacturer.getName(), savedManufacturer.getCountry());

        beerRepository.create(new Beer(null, "Zeta", 5.0, "IPA", "desc", manufacturer, "imageUrl", null));
        beerRepository.create(new Beer(null, "Ambar", 4.7, "Lager", "desc", manufacturer, "imageUrl", null));

        var sort = new BeerSort(SortDirection.ASC.toString(), "name");
        var result = beerRepository.findAll(sort, 0, 10);

        assertThat(result.getContent().get(0).name()).isEqualTo("Ambar");
    }

    @Transactional
    @Test
    @Order(3)
    void update_shouldModifyExistingBeer() {
        var savedManufacturer = manufacturerRepository.save(new ManufacturerJPAEntity(null, "Mahou", "Spain"));
        var manufacturer = new Manufacturer(savedManufacturer.getId(), savedManufacturer.getName(), savedManufacturer.getCountry());

        var beer = new Beer(null, "Mahou 5 Estrellas", 5.5, "Lager", "desc", manufacturer, "imageUrl", null);
        var saved = beerRepository.create(beer);

        var updatedBeer = new Beer(saved.id(), "Mahou Clásica", 4.9, "Lager", "actualizada", manufacturer, "updatedImageUrl", null);

        var result = beerRepository.update(saved.id(), updatedBeer);

        assertThat(result.id()).isEqualTo(saved.id());
        assertThat(result.name()).isEqualTo("Mahou Clásica");
        assertThat(result.abv()).isEqualTo(4.9);
        assertThat(result.description()).isEqualTo("actualizada");
    }

    @Transactional
    @Test
    @Order(4)
    void delete_shouldRemoveBeer() {
        var savedManufacturer = manufacturerRepository.save(new ManufacturerJPAEntity(null, "Mahou", "Spain"));
        var manufacturer = new Manufacturer(savedManufacturer.getId(), savedManufacturer.getName(), savedManufacturer.getCountry());

        var beer = new Beer(null, "Mahou Negra", 6.5, "Stout", "desc", manufacturer, "imageUrl", null);
        var saved = beerRepository.create(beer);

        beerRepository.delete(saved.id());

        assertThat(beerRepository.findById(saved.id())).isNotPresent();
    }

    @Transactional
    @Test
    @Order(5)
    void search_shouldReturnFilteredResults() {
        var savedManufacturer = manufacturerRepository.save(new ManufacturerJPAEntity(null, "Mahou", "Spain"));
        var manufacturer = new Manufacturer(savedManufacturer.getId(), savedManufacturer.getName(), savedManufacturer.getCountry());

        beerRepository.create(new Beer(null, "Mahou IPA", 5.5, "IPA", "desc", manufacturer, "imageUrl", null));
        beerRepository.create(new Beer(null, "Mahou Clásica", 4.9, "Lager", "desc", manufacturer, "imageUrl", null));
        beerRepository.create(new Beer(null, "Estrella Galicia", 5.0, "Lager", "desc", manufacturer, "imageUrl", null));

        var criteria = new BeerSearchCriteria("Mahou", null, null, null);
        var sort = new BeerSort(SortDirection.ASC.toString(), "name");
        var result = beerRepository.search(criteria, sort, 0, 10);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).name()).contains("Clásica");
    }



}