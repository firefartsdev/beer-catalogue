package haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto;

import haufe.group.beer_catalogue.infrastructure.adapter.rest.manufacturer.ManufacturerDTO;

import java.util.UUID;

public record BeerDetailDTO(UUID id, String name, Double abv, String type, String description, ManufacturerDTO manufacturer, String image) {
}
