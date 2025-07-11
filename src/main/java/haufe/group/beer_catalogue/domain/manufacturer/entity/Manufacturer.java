package haufe.group.beer_catalogue.domain.manufacturer.entity;

import java.util.UUID;

public record Manufacturer(UUID id, String name, String country) {
}
