package haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto;

import haufe.group.beer_catalogue.infrastructure.adapter.rest.manufacturer.ManufacturerDTO;

import java.util.UUID;

public record BeerDTO(UUID id, String name, Double abv, String type, String description, ManufacturerDTO manufacturer) {
}
