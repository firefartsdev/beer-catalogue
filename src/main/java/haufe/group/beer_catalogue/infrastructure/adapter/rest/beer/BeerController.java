package haufe.group.beer_catalogue.infrastructure.adapter.rest.beer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    @GetMapping
    public ResponseEntity<List<BeerDTO>> list() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{beerId}")
    @PostMapping
    public ResponseEntity<BeerDTO> get(@PathVariable UUID id) {
        return ResponseEntity.ok(null);
    }

    public ResponseEntity<BeerDTO> create(@RequestBody BeerDTO beerDTO) {
        return ResponseEntity.status(CREATED).body(null);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDTO> update(@PathVariable UUID id, @RequestBody BeerDTO beerDTO) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return ResponseEntity.noContent().build();
    }

}
