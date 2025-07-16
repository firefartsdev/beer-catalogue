package haufe.group.beer_catalogue.infrastructure.adapter.rest.manufacturer;

import haufe.group.beer_catalogue.application.manufacturer.*;
import haufe.group.beer_catalogue.domain.manufacturer.vo.ManufacturerSort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/manufacturers")
public class ManufacturerController {

    private final ManufacturerDTOMapper manufacturerDTOMapper;

    private final CreateManufacturerUseCase createManufacturerUseCase;
    private final ListManufacturersUseCase listManufacturersUseCase;
    private final GetManufacturerUseCase getManufacturerUseCase;
    private final UpdateManufacturerUseCase updateManufacturerUseCase;
    private final DeleteManufacturerUseCase deleteManufacturerUseCase;

    @GetMapping
    public ResponseEntity<Page<ManufacturerDTO>> list(
            @RequestParam(defaultValue = "name") final String sortBy,
            @RequestParam(defaultValue = "ASC") final String order,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size
    ) {
        final var sort = new ManufacturerSort(order, sortBy);
        final var manufacturers = this.listManufacturersUseCase.listManufacturers(sort, page, size);
        final var paginatedManufacturers = manufacturers.map(this.manufacturerDTOMapper::toDTO);
        return ResponseEntity.ok(paginatedManufacturers);
    }

    @GetMapping("/{manufacturerId}")
    public ResponseEntity<ManufacturerDTO> get(@PathVariable final UUID manufacturerId) {
        final var manufacturer = this.getManufacturerUseCase.getManufacturer(manufacturerId);
        return ResponseEntity.ok(this.manufacturerDTOMapper.toDTO(manufacturer));
    }

    @PostMapping
    public ResponseEntity<ManufacturerDTO> create(@RequestBody @Valid final ManufacturerDTO manufacturerDTO) {
        final var createdManufacturer = this.createManufacturerUseCase.createManufacturer(this.manufacturerDTOMapper.toDomain(manufacturerDTO));
        return ResponseEntity.status(CREATED).body(this.manufacturerDTOMapper.toDTO(createdManufacturer));
    }

    @PutMapping("{manufacturerId}")
    public ResponseEntity<ManufacturerDTO> update(@PathVariable final UUID manufacturerId, @RequestBody final ManufacturerDTO manufacturerDTO) {
        final var updatedManufacturer = this.updateManufacturerUseCase.updateManufacturer(manufacturerId, this.manufacturerDTOMapper.toDomain(manufacturerDTO));
        return ResponseEntity.ok(this.manufacturerDTOMapper.toDTO(updatedManufacturer));
    }

    @DeleteMapping("/{manufacturerId}")
    public ResponseEntity<Void> delete(@PathVariable final UUID manufacturerId) {
        this.deleteManufacturerUseCase.deleteManufacturer(manufacturerId);
        return ResponseEntity.noContent().build();
    }

}
