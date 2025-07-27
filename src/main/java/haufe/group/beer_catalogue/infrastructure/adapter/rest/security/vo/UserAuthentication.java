package haufe.group.beer_catalogue.infrastructure.adapter.rest.security.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserAuthentication(String role,
                                 @NotNull(message = "User is mandatory")
                                 UUID userId) {
}
