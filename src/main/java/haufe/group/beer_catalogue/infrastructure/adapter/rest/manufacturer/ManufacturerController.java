package haufe.group.beer_catalogue.infrastructure.adapter.rest.manufacturer;

import haufe.group.beer_catalogue.application.manufacturer.CreateManufacturerUseCase;
import haufe.group.beer_catalogue.application.manufacturer.GetManufacturerUseCase;
import haufe.group.beer_catalogue.application.manufacturer.ListManufacturersUseCase;
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

    @GetMapping
    public ResponseEntity<List<ManufacturerDTO>> list() {
        final var manufacturers = this.listManufacturersUseCase.listManufacturers();
        return ResponseEntity.ok(this.manufacturerDTOMapper.toDTOList(manufacturers));
    }

    @GetMapping("/{manufacturerId}")
    public ResponseEntity<ManufacturerDTO> get(@PathVariable UUID manufacturerId) {
        final var manufacturer = this.getManufacturerUseCase.getManufacturer(manufacturerId);
        return ResponseEntity.ok(this.manufacturerDTOMapper.toDTO(manufacturer));
    }

    @PostMapping
    public ResponseEntity<ManufacturerDTO> create(@RequestBody ManufacturerDTO manufacturerDTO) {
        final var createdManufacturer = this.createManufacturerUseCase.createManufacturer(this.manufacturerDTOMapper.toDomain(manufacturerDTO));
        return ResponseEntity.status(CREATED).body(this.manufacturerDTOMapper.toDTO(createdManufacturer));
    }

    @PutMapping("{manufacturerId}")
    public ResponseEntity<ManufacturerDTO> update(@PathVariable UUID manufacturerId, @RequestBody ManufacturerDTO manufacturerDTO) {
        return ResponseEntity.ok(new ManufacturerDTO(manufacturerId, "updated name", "updated country"));
    }

    @DeleteMapping("/{manufacturerId}")
    public ResponseEntity<Void> delete(@PathVariable UUID manufacturerId) {
        return ResponseEntity.noContent().build();
    }

}
