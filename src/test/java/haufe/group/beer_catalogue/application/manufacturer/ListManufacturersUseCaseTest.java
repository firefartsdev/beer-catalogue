package haufe.group.beer_catalogue.application.manufacturer;

import haufe.group.beer_catalogue.application.manufacturer.usecase.ListManufacturersUseCase;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        List<Manufacturer> expected = List.of(new Manufacturer(UUID.randomUUID(), "Brew Co", "Spain"));

        when(manufacturerRepository.findAll(sort)).thenReturn(expected);

        List<Manufacturer> result = useCase.listManufacturers(sort);

        assertEquals(expected, result);
        verify(manufacturerRepository).findAll(sort);
    }
}