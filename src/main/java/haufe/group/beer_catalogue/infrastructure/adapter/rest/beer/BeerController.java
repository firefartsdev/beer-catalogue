package haufe.group.beer_catalogue.infrastructure.adapter.rest.beer;

import haufe.group.beer_catalogue.application.beer.*;
import haufe.group.beer_catalogue.application.manufacturer.ManufacturerSort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/beers")
@RequiredArgsConstructor
public class BeerController {

    private final ListBeersUseCase listBeersUseCase;
    private final GetBeerUseCase getBeerUseCase;
    private final CreateBeerUseCase createBeerUseCase;
    private final UpdateBeerUseCase updateBeerUseCase;
    private final DeleteBeerUseCase deleteBeerUseCase;

    private final BeerDTOMapper beerDTOMapper;

    @GetMapping
    public ResponseEntity<List<BeerDTO>> list(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String order
    ) {
        final var sort = new BeerSort(order, sortBy);
        final var beers = this.listBeersUseCase.listBeers(sort);
        return ResponseEntity.ok(this.beerDTOMapper.toDTOList(beers));
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDTO> get(@PathVariable UUID beerId) {
        final var beer = this.getBeerUseCase.getBeer(beerId);
        return ResponseEntity.ok(this.beerDTOMapper.toDTO(beer));
    }

    @PostMapping
    public ResponseEntity<BeerDTO> create(@RequestBody CreateBeerRequestDTO createBeerRequestDTO) {
        final var createdBeer = this.createBeerUseCase.createBeer(this.beerDTOMapper.toDomainFromCreate(createBeerRequestDTO));
        return ResponseEntity.status(CREATED).body(this.beerDTOMapper.toDTO(createdBeer));
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDTO> update(@PathVariable UUID beerId, @RequestBody BeerDTO beerDTO) {
        final var updatedBeer = this.updateBeerUseCase.updateBeer(beerId, this.beerDTOMapper.toDomain(beerDTO));
        return ResponseEntity.ok(this.beerDTOMapper.toDTO(updatedBeer));
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> delete(@PathVariable UUID beerId) {
        this.deleteBeerUseCase.deleteBeer(beerId);
        return ResponseEntity.noContent().build();
    }

}
