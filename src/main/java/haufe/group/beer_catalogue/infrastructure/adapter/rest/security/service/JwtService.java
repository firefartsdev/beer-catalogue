package haufe.group.beer_catalogue.infrastructure.adapter.rest.security.service;

import haufe.group.beer_catalogue.infrastructure.adapter.rest.security.vo.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    public String generateToken(final Role role, final UUID userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Role extractRole(final String token) {
        final var roleName = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);

        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            return Role.ANONYMOUS;
        }
    }

    public Role extractRoleFromRoleClaim(final String roleClaim) {
        final var roleName = roleClaim.substring("ROLE_".length());
        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            return Role.ANONYMOUS;
        }
    }

    public String extractUserId(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("sub", String.class);
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
