package haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer;

import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import haufe.group.beer_catalogue.domain.manufacturer.vo.ManufacturerSort;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.SortDirection;
import haufe.group.beer_catalogue.infrastructure.adapter.s3.S3Repository;
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

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test-nos3")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManufacturerRepositoryIT {

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
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private ManufacturerJPARepository manufacturerJPARepository;

    @BeforeEach
    void cleanDatabase() {
        manufacturerJPARepository.deleteAll();
    }

    @Transactional
    @Test
    @Order(1)
    void create_and_findById_shouldWork() {
        var manufacturer = new Manufacturer(null, "Estrella Galicia", "Spain");

        var saved = manufacturerRepository.create(manufacturer);

        assertThat(saved.id()).isNotNull();

        var fromDb = manufacturerRepository.findById(saved.id());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().name()).isEqualTo("Estrella Galicia");
        assertThat(fromDb.get().country()).isEqualTo("Spain");
    }

    @Transactional
    @Test
    @Order(2)
    void findAll_shouldReturnSorted() {
        manufacturerRepository.create(new Manufacturer(null, "Zeta", "Spain"));
        manufacturerRepository.create(new Manufacturer(null, "Ambar", "Spain"));

        var sort = new ManufacturerSort(SortDirection.ASC.toString(), "name");
        var result = manufacturerRepository.findAll(sort, 0, 10);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).name()).isEqualTo("Ambar");
        assertThat(result.getContent().get(1).name()).isEqualTo("Zeta");
    }

    @Transactional
    @Test
    @Order(3)
    void update_shouldModifyManufacturer() {
        var original = manufacturerRepository.create(new Manufacturer(null, "Estrella", "Spain"));

        var updated = new Manufacturer(original.id(), "Estrella Damm", "Catalonia");

        var result = manufacturerRepository.update(original.id(), updated);

        assertThat(result.name()).isEqualTo("Estrella Damm");
        assertThat(result.country()).isEqualTo("Catalonia");

        var fromDb = manufacturerRepository.findById(original.id());

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().name()).isEqualTo("Estrella Damm");
    }

    @Transactional
    @Test
    @Order(4)
    void delete_shouldRemoveManufacturer() {
        var manufacturer = manufacturerRepository.create(new Manufacturer(null, "San Miguel", "Spain"));

        manufacturerRepository.delete(manufacturer.id());

        assertThat(manufacturerRepository.findById(manufacturer.id())).isNotPresent();
    }
}