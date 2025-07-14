package haufe.group.beer_catalogue.application.beer;

import haufe.group.beer_catalogue.application.exception.EntityNotFoundException;
import haufe.group.beer_catalogue.domain.beer.entity.Beer;
import haufe.group.beer_catalogue.domain.beer.port.BeerRepository;
import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;
import haufe.group.beer_catalogue.domain.manufacturer.port.ManufacturerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBeerUseCaseTest {

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private UpdateBeerUseCase updateBeerUseCase;

    @Test
    void shouldUpdateBeerWhenExistsAndManufacturerIsSame() {
        UUID beerId = UUID.randomUUID();
        UUID manufacturerId = UUID.randomUUID();
        Manufacturer manufacturer = new Manufacturer(manufacturerId, "Brew Co", "Spain");

        Beer existingBeer = new Beer(beerId, "Old", 4.5, "Ale", "Old desc", manufacturer);
        Beer updatedBeer = new Beer(beerId, "New", 5.0, "Lager", "New desc", manufacturer);

        when(beerRepository.findById(beerId)).thenReturn(Optional.of(existingBeer));
        when(beerRepository.update(beerId, updatedBeer)).thenReturn(updatedBeer);

        Beer result = updateBeerUseCase.updateBeer(beerId, updatedBeer);

        assertEquals(updatedBeer, result);
    }

    @Test
    void shouldCheckNewManufacturerIfDifferent() {
        UUID beerId = UUID.randomUUID();
        Manufacturer oldManufacturer = new Manufacturer(UUID.randomUUID(), "Old", "Spain");
        Manufacturer newManufacturer = new Manufacturer(UUID.randomUUID(), "New", "Spain");

        Beer existingBeer = new Beer(beerId, "Old", 4.5, "Ale", "Old desc", oldManufacturer);
        Beer updatedBeer = new Beer(beerId, "New", 5.0, "Lager", "New desc", newManufacturer);

        when(beerRepository.findById(beerId)).thenReturn(Optional.of(existingBeer));
        when(manufacturerRepository.findById(newManufacturer.id())).thenReturn(Optional.of(newManufacturer));
        when(beerRepository.update(beerId, updatedBeer)).thenReturn(updatedBeer);

        Beer result = updateBeerUseCase.updateBeer(beerId, updatedBeer);

        assertEquals(updatedBeer, result);
        verify(manufacturerRepository).findById(newManufacturer.id());
    }

    @Test
    void shouldThrowExceptionIfBeerDoesNotExist() {
        UUID beerId = UUID.randomUUID();
        Manufacturer manufacturer = new Manufacturer(UUID.randomUUID(), "Brew Co", "Spain");
        Beer beer = new Beer(beerId, "New", 5.0, "Lager", "Desc", manufacturer);

        when(beerRepository.findById(beerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                updateBeerUseCase.updateBeer(beerId, beer)
        );
    }

    @Test
    void shouldThrowExceptionIfNewManufacturerDoesNotExist() {
        UUID beerId = UUID.randomUUID();
        Manufacturer oldManu = new Manufacturer(UUID.randomUUID(), "Old", "Spain");
        Manufacturer newManu = new Manufacturer(UUID.randomUUID(), "New", "Spain");

        Beer existing = new Beer(beerId, "Old", 4.0, "IPA", "desc", oldManu);
        Beer update = new Beer(beerId, "New", 5.0, "Pale", "desc2", newManu);

        when(beerRepository.findById(beerId)).thenReturn(Optional.of(existing));
        when(manufacturerRepository.findById(newManu.id())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                updateBeerUseCase.updateBeer(beerId, update)
        );
    }
}