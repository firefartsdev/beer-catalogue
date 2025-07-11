package haufe.group.beer_catalogue.infrastructure.adapter.rest.manufacturer;

import java.util.UUID;

public record ManufacturerDTO(UUID id, String name, String country) {
}
