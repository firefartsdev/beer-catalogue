package haufe.group.beer_catalogue.infrastructure.adapter.rest.security;

import haufe.group.beer_catalogue.infrastructure.adapter.rest.security.service.JwtService;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.security.vo.Role;
import haufe.group.beer_catalogue.infrastructure.adapter.rest.security.vo.UserAuthentication;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody @Valid final UserAuthentication userAuthentication) {
        Role assignedRole;
        try {
            assignedRole = Role.valueOf(Optional.ofNullable(userAuthentication.role()).orElse("ANONYMOUS").toUpperCase());
        } catch (final IllegalArgumentException e) {
            assignedRole = Role.ANONYMOUS;
        }

        String token = jwtService.generateToken(assignedRole, userAuthentication.userId());
        return ResponseEntity.ok(Map.of(
                "role", assignedRole.name(),
                "token", token
        ));
    }
}
