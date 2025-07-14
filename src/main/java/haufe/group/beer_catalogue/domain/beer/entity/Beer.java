package haufe.group.beer_catalogue.domain.beer.entity;

import haufe.group.beer_catalogue.domain.manufacturer.entity.Manufacturer;

import java.util.UUID;

public record Beer(UUID id, String name, Double abv, String type, String description, Manufacturer manufacturer, String imageUrl, String imageBase64) {
}
