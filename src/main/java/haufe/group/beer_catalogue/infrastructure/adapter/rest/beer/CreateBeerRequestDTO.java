package haufe.group.beer_catalogue.infrastructure.adapter.rest.beer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateBeerRequestDTO(
        @NotBlank(message = "Cannot be empty")
        @NotNull(message = "Mandatory field")
        String name,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = true, message = "Cannot be negative")
        Double abv,
        @NotBlank(message = "Cannot be empty")
        @NotNull(message = "Mandatory field")
        String type,
        String description,
        @NotNull
        UUID manufacturerId
) {
}
