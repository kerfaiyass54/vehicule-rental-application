package com.projecttuto.vehicule_rental.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final KeycloakJwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // ============================================================
                // CORS
                // ============================================================
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ============================================================
                // CSRF
                // ============================================================
                // Stateless JWT API -> CSRF is not required.
                .csrf(csrf -> csrf.disable())

                // ============================================================
                // SESSION MANAGEMENT
                // ============================================================
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ============================================================
                // AUTHORIZATION
                // ============================================================
                .authorizeHttpRequests(auth -> auth

                        // ----------------------------------------------------
                        // PUBLIC ENDPOINTS
                        // ----------------------------------------------------

                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()

                        // OPTIONS -> CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        // ----------------------------------------------------
                        // ADMIN
                        // ----------------------------------------------------

                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // ----------------------------------------------------
                        // CLIENT
                        // ----------------------------------------------------

                        .requestMatchers("/api/client/**")
                        .hasRole("CLIENT")

                        // ----------------------------------------------------
                        // SUPPLIER
                        // ----------------------------------------------------

                        .requestMatchers("/api/supplier/**")
                        .hasRole("SUPPLIER")

                        // ----------------------------------------------------
                        // REPAIR
                        // ----------------------------------------------------

                        .requestMatchers("/api/repair/**")
                        .hasRole("REPAIR")

                        // ----------------------------------------------------
                        // EVERYTHING ELSE
                        // ----------------------------------------------------

                        .anyRequest()
                        .authenticated()
                )

                // ============================================================
                // OAUTH2 RESOURCE SERVER
                // ============================================================
                .oauth2ResourceServer(oauth2 -> oauth2

                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(
                                        jwtAuthenticationConverter
                                )
                        )

                        .authenticationEntryPoint(
                                new BearerTokenAuthenticationEntryPoint()
                        )

                        .accessDeniedHandler(
                                new BearerTokenAccessDeniedHandler()
                        )
                )

                // ============================================================
                // EXCEPTION HANDLING
                // ============================================================
                .exceptionHandling(exception -> exception

                        .defaultAuthenticationEntryPointFor(
                                new BearerTokenAuthenticationEntryPoint(),
                                new MediaTypeRequestMatcher(
                                        MediaType.APPLICATION_JSON
                                )
                        )

                        .defaultAccessDeniedHandlerFor(
                                new BearerTokenAccessDeniedHandler(),
                                new MediaTypeRequestMatcher(
                                        MediaType.APPLICATION_JSON
                                )
                        )
                );

        return http.build();
    }


    // ========================================================================
    // CORS CONFIGURATION
    // ========================================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200"
        ));

        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        configuration.setExposedHeaders(List.of(
                "Authorization"
        ));

        configuration.setAllowCredentials(true);

        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}