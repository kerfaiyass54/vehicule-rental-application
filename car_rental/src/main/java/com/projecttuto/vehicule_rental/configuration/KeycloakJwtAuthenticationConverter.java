package com.projecttuto.vehicule_rental.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KeycloakJwtAuthenticationConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES = "roles";

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    private final String accessResource;


    public KeycloakJwtAuthenticationConverter(
            @Value("${resource.access}") String accessResource) {

        this.accessResource = accessResource;
    }


    @Override
    public AbstractAuthenticationToken convert(
            @NonNull Jwt jwt) {

        Collection<GrantedAuthority> authorities =
                Stream.concat(

                                // Standard JWT authorities
                                jwtGrantedAuthoritiesConverter
                                        .convert(jwt)
                                        .stream(),

                                // Keycloak resource roles
                                extractResourceRoles(jwt)
                                        .stream()

                        )
                        .collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                getPrincipalName(jwt)
        );
    }


    /**
     * Extracts roles from:
     *
     * resource_access:
     *   <client-id>:
     *      roles:
     *        - ADMIN
     *        - CLIENT
     *        - SUPPLIER
     *        - REPAIR
     */
    private Collection<? extends GrantedAuthority> extractResourceRoles(
            Jwt jwt) {

        Map<String, Object> resourceAccess =
                jwt.getClaim(RESOURCE_ACCESS);

        if (resourceAccess == null) {
            return Collections.emptySet();
        }

        Object resourceObject =
                resourceAccess.get(accessResource);

        if (!(resourceObject instanceof Map<?, ?> resource)) {
            return Collections.emptySet();
        }

        Object rolesObject =
                resource.get(ROLES);

        if (!(rolesObject instanceof Collection<?> roles)) {
            return Collections.emptySet();
        }

        return roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(this::normalizeRole)
                .map(role ->
                        new SimpleGrantedAuthority(
                                ROLE_PREFIX + role
                        )
                )
                .collect(Collectors.toSet());
    }


    /**
     * Normalizes Keycloak role names.
     *
     * Examples:
     *
     * admin     -> ADMIN
     * client    -> CLIENT
     * supplier  -> SUPPLIER
     * repair    -> REPAIR
     */
    private String normalizeRole(String role) {

        return role
                .trim()
                .replace("-", "_")
                .toUpperCase();
    }


    /**
     * Defines the authenticated user's principal name.
     *
     * Preferred:
     * email
     *
     * Fallback:
     * preferred_username
     *
     * Fallback:
     * subject
     */
    private String getPrincipalName(Jwt jwt) {

        String email = jwt.getClaimAsString("email");

        if (email != null && !email.isBlank()) {
            return email;
        }

        String username =
                jwt.getClaimAsString("preferred_username");

        if (username != null && !username.isBlank()) {
            return username;
        }

        return jwt.getSubject();
    }
}