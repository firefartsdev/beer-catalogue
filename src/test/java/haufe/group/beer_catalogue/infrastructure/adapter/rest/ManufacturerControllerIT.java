package haufe.group.beer_catalogue.infrastructure.adapter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer.ManufacturerJPAEntity;
import haufe.group.beer_catalogue.infrastructure.adapter.persistence.manufacturer.ManufacturerJPARepository;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.manufacturer.ManufacturerDTO;
import haufe.group.beer_catalogue.infrastructure.adapter.s3.S3Repository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManufacturerControllerIT {

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
    private ManufacturerJPARepository manufacturerRepository;

    private static UUID savedManufacturerId;

    @BeforeAll
    void cleanDb() {
        manufacturerRepository.deleteAll();
    }

    @Test
    @Order(1)
    void should_create_manufacturer() throws Exception {
        var request = new ManufacturerDTO(null, "BrewDog", "UK");

        var result = mockMvc.perform(post("/api/v1/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("BrewDog"))
                .andReturn();

        var json = objectMapper.readTree(result.getResponse().getContentAsString());
        savedManufacturerId = UUID.fromString(json.get("id").asText());

        assertThat(savedManufacturerId).isNotNull();
    }

    @Test
    @Order(2)
    void should_get_manufacturer_by_id() throws Exception {
        mockMvc.perform(get("/api/v1/manufacturers/{id}", savedManufacturerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BrewDog"));
    }

    @Test
    @Order(3)
    void should_list_manufacturers_sorted() throws Exception {
        mockMvc.perform(get("/api/v1/manufacturers")
                        .param("sortBy", "name")
                        .param("order", "ASC")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @Order(4)
    void should_update_manufacturer() throws Exception {
        var updated = new ManufacturerDTO(savedManufacturerId, "BrewDog Updated", "UK");

        mockMvc.perform(put("/api/v1/manufacturers/{id}", savedManufacturerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated))
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BrewDog Updated"));
    }

    @Test
    @Order(5)
    void should_delete_manufacturer() throws Exception {
        mockMvc.perform(delete("/api/v1/manufacturers/{id}", savedManufacturerId)
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/manufacturers/{id}", savedManufacturerId))
                .andExpect(status().is4xxClientError());
    }
}