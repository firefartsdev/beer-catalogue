package haufe.group.beer_catalogue.infrastructure.adapter.rest.security;

import haufe.group.beer_catalogue.infrastructure.adapter.rest.security.service.RoleHeaderAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class RestSecurityConfig {

    private final RoleHeaderAuthenticationFilter roleHeaderAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionCreationPolicy -> sessionCreationPolicy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(roleHeaderAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(GET, "/api/v1/beers/**", "/api/v1/manufacturers/**").permitAll()
                        .requestMatchers("/api/v1/beers/search").permitAll()
                        .requestMatchers("/api/v1/authentication").permitAll()
                        .requestMatchers("/api/v1/manufacturers/**").hasAnyRole(Role.MANUFACTURER.name(), Role.ADMIN.name())
                        .anyRequest().hasRole(Role.ADMIN.name())
                );

        return http.build();
    }
}
