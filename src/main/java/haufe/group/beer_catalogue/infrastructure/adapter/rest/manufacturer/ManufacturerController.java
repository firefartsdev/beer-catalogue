package haufe.group.beer_catalogue.infrastructure.adapter.rest.manufacturer;

import haufe.group.beer_catalogue.application.manufacturer.*;
import haufe.group.beer_catalogue.application.manufacturer.ManufacturerSort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<ManufacturerDTO>> list(
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String order
    ) {
        final var sort = new ManufacturerSort(order, sortBy);
        final var manufacturers = this.listManufacturersUseCase.listManufacturers(sort);
        return ResponseEntity.ok(this.manufacturerDTOMapper.toDTOList(manufacturers));
    }

    @GetMapping("/{manufacturerId}")
    public ResponseEntity<ManufacturerDTO> get(@PathVariable UUID manufacturerId) {
        final var manufacturer = this.getManufacturerUseCase.getManufacturer(manufacturerId);
        return ResponseEntity.ok(this.manufacturerDTOMapper.toDTO(manufacturer));
    }

    @PostMapping
    public ResponseEntity<ManufacturerDTO> create(@RequestBody @Valid ManufacturerDTO manufacturerDTO) {
        final var createdManufacturer = this.createManufacturerUseCase.createManufacturer(this.manufacturerDTOMapper.toDomain(manufacturerDTO));
        return ResponseEntity.status(CREATED).body(this.manufacturerDTOMapper.toDTO(createdManufacturer));
    }

    @PutMapping("{manufacturerId}")
    public ResponseEntity<ManufacturerDTO> update(@PathVariable UUID manufacturerId, @RequestBody ManufacturerDTO manufacturerDTO) {
        final var updatedManufacturer = this.updateManufacturerUseCase.updateManufacturer(manufacturerId, this.manufacturerDTOMapper.toDomain(manufacturerDTO));
        return ResponseEntity.ok(this.manufacturerDTOMapper.toDTO(updatedManufacturer));
    }

    @DeleteMapping("/{manufacturerId}")
    public ResponseEntity<Void> delete(@PathVariable UUID manufacturerId) {
        this.deleteManufacturerUseCase.deleteManufacturer(manufacturerId);
        return ResponseEntity.noContent().build();
    }

}
