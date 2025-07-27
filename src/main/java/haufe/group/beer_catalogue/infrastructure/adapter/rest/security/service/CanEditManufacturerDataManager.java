package haufe.group.beer_catalogue.infrastructure.adapter.rest.security.service;

import haufe.group.beer_catalogue.infrastructure.adapter.rest.security.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component("accessChecker")
@RequiredArgsConstructor
public class CanEditManufacturerDataManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final JwtService jwtService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        final var authentication = authenticationSupplier.get();
        if(authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        final var role = this.jwtService.extractRoleFromRoleClaim(authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(""));

        if(role.equals(Role.ADMIN)) return new AuthorizationDecision(true);

        if(role.equals(Role.MANUFACTURER)) {
            final var uri = context.getRequest().getRequestURI();
            final var resourceId = uri.substring(uri.lastIndexOf('/') + 1);
            final var userId = authentication.getPrincipal().toString();
            return new AuthorizationDecision(resourceId.equals(userId));
        }

        return new AuthorizationDecision(false);
    }
}
