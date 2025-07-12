package haufe.group.beer_catalogue.infrastructure.adapter.rest.manufacturer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ManufacturerDTO(
        UUID id,
        @NotNull(message = "Field is mandatory")
        @NotBlank(message = "Field cannot be empty")
        String name,
        @NotNull(message = "Field is mandatory")
        @NotBlank(message = "Field cannot be empty")
        String country) {
}
