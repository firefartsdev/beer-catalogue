package haufe.group.beer_catalogue.infrastructure.adapter.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.beer.BeerJPARepository;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer.ManufacturerJPAEntity;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer.ManufacturerJPARepository;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto.BeerDTO;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto.CreateBeerRequestDTO;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto.BeerSearchRequestDTO;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.manufacturer.ManufacturerDTO;
import haufe.group.beer_catalogue.infrastructure.adapter.s3.S3Repository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test-nos3")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BeerControllerIT {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @MockitoBean
    private S3Repository s3Repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BeerJPARepository beerRepository;

    @Autowired
    private ManufacturerJPARepository manufacturerRepository;

    private static UUID savedBeerId;
    private static UUID savedManufacturerId;

    @BeforeAll
    void clean() {
        beerRepository.deleteAll();
        manufacturerRepository.deleteAll();
    }

    @Test
    @Order(1)
    void should_create_beer() throws Exception {
        var manufacturer = manufacturerRepository.save(new ManufacturerJPAEntity(null, "Estrella Galicia", "Spain"));
        savedManufacturerId = manufacturer.getId();

        var beerData = new CreateBeerRequestDTO();
        beerData.setName("1906");
        beerData.setAbv(6.5);
        beerData.setType("Lager");
        beerData.setDescription("Reserva Especial");
        beerData.setManufacturerId(manufacturer.getId());
        var multipartFile = new MockMultipartFile("image", "beer.jpg", "image/jpeg", "fake-image-content".getBytes());

        var result = mockMvc.perform(multipart("/api/v1/beers")
                        .file(multipartFile)
                        .param("name", "1906")
                        .param("abv", "6.5")
                        .param("type", "Lager")
                        .param("description", "Reserva Especial")
                        .param("manufacturerId", manufacturer.getId().toString())
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isCreated())
                .andReturn();

        var created = objectMapper.readTree(result.getResponse().getContentAsString());
        savedBeerId = UUID.fromString(created.get("id").asText());

        assertThat(savedBeerId).isNotNull();
    }

    @Test
    @Order(2)
    void should_get_beer_by_id() throws Exception {
        mockMvc.perform(get("/api/v1/beers/{id}", savedBeerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("1906"));
    }

    @Test
    @Order(3)
    void should_list_beers_sorted() throws Exception {
        mockMvc.perform(get("/api/v1/beers")
                        .param("sortBy", "name")
                        .param("order", "ASC")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @Order(4)
    void should_update_beer() throws Exception {
        var manufacturer = new ManufacturerDTO(savedManufacturerId, null, null);
        var updated = new BeerDTO(savedBeerId, "1906 Red Vintage", 6.5, "Lager", "La colorada", manufacturer, "someUrl", null);

        mockMvc.perform(put("/api/v1/beers/{id}", savedBeerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated))
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("1906 Red Vintage"));
    }

    @Test
    @Order(5)
    void should_search_beers() throws Exception {
        var criteria = new BeerSearchRequestDTO("1906", null, null, null, 0, 10, "ASC", "name");

        mockMvc.perform(post("/api/v1/beers/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criteria))
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("1906 Red Vintage"));
    }

    @Test
    @Order(6)
    void should_delete_beer() throws Exception {
        mockMvc.perform(delete("/api/v1/beers/{id}", savedBeerId)
                .header("X-User-Role", "ADMIN"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/beers/{id}", savedBeerId))
                .andExpect(status().is4xxClientError());
    }
}
