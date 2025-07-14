package haufe.group.beer_catalogue.infrastructure.adapter.rest.beer.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class CreateBeerRequestDTO {

        @NotBlank(message = "Cannot be empty")
        @NotNull(message = "Mandatory field")
        private String name;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true, message = "Cannot be negative")
        private Double abv;

        @NotBlank(message = "Cannot be empty")
        @NotNull(message = "Mandatory field")
        private String type;

        private String description;

        @NotNull
        private UUID manufacturerId;

        @NotNull
        private MultipartFile image;
}

