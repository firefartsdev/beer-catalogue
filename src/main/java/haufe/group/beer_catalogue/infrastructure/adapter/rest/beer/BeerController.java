package haufe.group.beer_catalogue.infrastructure.adapter.rest.beer;

import haufe.group.beer_catalogue.application.beer.*;
import haufe.group.beer_catalogue.domain.beer.vo.BeerSort;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto.BeerDTO;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto.BeerSearchRequestDTO;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto.CreateBeerRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final SearchBeersUseCase searchBeersUseCase;

    private final BeerDTOMapper beerDTOMapper;

    @GetMapping
    public ResponseEntity<Page<BeerDTO>> list(
            @RequestParam(defaultValue = "name") final String sortBy,
            @RequestParam(defaultValue = "ASC") final String order,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size

    ) {
        final var sort = new BeerSort(order, sortBy);
        final var beers = this.listBeersUseCase.listBeers(sort, page, size);
        final var paginatedBeers = beers.map(this.beerDTOMapper::toDTO);
        return ResponseEntity.ok(paginatedBeers);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDTO> get(@PathVariable final UUID beerId) {
        final var beer = this.getBeerUseCase.getBeer(beerId);
        return ResponseEntity.ok(this.beerDTOMapper.toDTO(beer));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BeerDTO> create(@ModelAttribute @Valid final CreateBeerRequestDTO createBeerRequestDTO) {
        final var createdBeer = this.createBeerUseCase.createBeer(this.beerDTOMapper.toDomainFromCreate(createBeerRequestDTO), createBeerRequestDTO.getImage());
        return ResponseEntity.status(CREATED).body(this.beerDTOMapper.toDTO(createdBeer));
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDTO> update(@PathVariable final UUID beerId, @RequestBody final BeerDTO beerDTO) {
        final var updatedBeer = this.updateBeerUseCase.updateBeer(beerId, this.beerDTOMapper.toDomain(beerDTO));
        return ResponseEntity.ok(this.beerDTOMapper.toDTO(updatedBeer));
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> delete(@PathVariable final UUID beerId) {
        this.deleteBeerUseCase.deleteBeer(beerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<BeerDTO>> search(@RequestBody final BeerSearchRequestDTO beerSearchRequestDTO) {
        final var criteria = this.beerDTOMapper.toCriteria(beerSearchRequestDTO);
        final var pageable = beerSearchRequestDTO.resolvedPageable();

        final var beers = this.searchBeersUseCase.searchBeers(criteria, beerSearchRequestDTO.resolvedSort(), pageable.getPageNumber(), pageable.getPageSize());
        final var paginatedBeers = beers.map(this.beerDTOMapper::toDTO);
        return ResponseEntity.ok(paginatedBeers);
    }

}
