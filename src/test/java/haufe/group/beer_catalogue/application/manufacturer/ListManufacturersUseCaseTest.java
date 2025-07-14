package haufe.group.beer_catalogue.application.manufacturer;

import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import haufe.group.beer_catalogue.domain.manufacturer.vo.ManufacturerSort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListManufacturersUseCaseTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ListManufacturersUseCase useCase;

    @Test
    void shouldListManufacturersWithSorting() {
        ManufacturerSort sort = new ManufacturerSort("ASC", "name");
        int page = 0;
        int size = 2;

        List<Manufacturer> expected = List.of(
                new Manufacturer(UUID.randomUUID(), "Brew Co", "Spain"),
                new Manufacturer(UUID.randomUUID(), "Brew Co 2", "Spain"));
        Page<Manufacturer> expectedPage = new PageImpl<>(expected, PageRequest.of(page, size), 10);

        when(manufacturerRepository.findAll(sort, page, size)).thenReturn(expectedPage);

        Page<Manufacturer> result = useCase.listManufacturers(sort, page, size);

        assertEquals(2, result.getContent().size());
        assertEquals("Brew Co 2", result.getContent().get(1).name());
        verify(manufacturerRepository).findAll(sort, page, size);
    }
}