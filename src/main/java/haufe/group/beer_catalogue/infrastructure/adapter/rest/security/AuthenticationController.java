package haufe.group.beer_catalogue.infrastructure.adapter.rest.security;

import haufe.group.beer_catalogue.infrastructure.adapter.rest.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Map<String, String>> authenticate(@RequestParam(required = false) String role) {
        Role assignedRole;
        try {
            assignedRole = Role.valueOf(Optional.ofNullable(role).orElse("ANONYMOUS").toUpperCase());
        } catch (final IllegalArgumentException e) {
            assignedRole = Role.ANONYMOUS;
        }

        String token = jwtService.generateToken(assignedRole);
        return ResponseEntity.ok(Map.of(
                "role", assignedRole.name(),
                "token", token
        ));
    }
}
