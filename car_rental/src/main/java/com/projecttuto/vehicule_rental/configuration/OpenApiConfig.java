package com.projecttuto.vehicule_rental.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";


    @Bean
    public OpenAPI vehicleRentalOpenAPI() {

        return new OpenAPI()

                // ============================================================
                // API INFORMATION
                // ============================================================

                .info(
                        new Info()
                                .title("Vehicle Rental Management API")
                                .version("2.0.0")

                                .description("""
                                        REST API for the Vehicle Rental Management System.

                                        The platform provides vehicle rental and
                                        management services for four types of users:

                                        • ADMIN
                                          Full administration and management access.

                                        • CLIENT
                                          Vehicle rental, buying, subscriptions,
                                          tickets and personal profile access.

                                        • SUPPLIER
                                          Vehicle, address, subscription, buying
                                          and repair-demand management.

                                        • REPAIR
                                          Repair tickets, demands and repair
                                          operations management.

                                        ------------------------------------------------

                                        Authentication:

                                        This API uses OAuth2 Resource Server
                                        authentication with JWT tokens issued
                                        by Keycloak.

                                        Send the access token using:

                                        Authorization: Bearer <JWT>

                                        ------------------------------------------------

                                        Roles:

                                        ADMIN
                                        CLIENT
                                        SUPPLIER
                                        REPAIR

                                        ------------------------------------------------

                                        Authorization:

                                        Every protected endpoint requires a valid
                                        JWT containing the appropriate Keycloak
                                        role.

                                        Public endpoints include authentication
                                        and OpenAPI documentation endpoints.
                                        """)
                                .contact(
                                        new Contact()
                                                .name("Vehicle Rental Development Team")
                                                .email("support@vehiclerental.com")
                                )
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url(
                                                        "https://www.apache.org/licenses/LICENSE-2.0"
                                                )
                                )
                )

                // ============================================================
                // EXTERNAL DOCUMENTATION
                // ============================================================

                .externalDocs(
                        new ExternalDocumentation()
                                .description(
                                        "Vehicle Rental API Documentation"
                                )
                )

                // ============================================================
                // SECURITY
                // ============================================================

                .components(
                        new Components()

                                .addSecuritySchemes(
                                        SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(
                                                        SecurityScheme.Type.HTTP
                                                )
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("""
                                                        JWT Bearer authentication.

                                                        Enter only the JWT token.

                                                        Swagger automatically sends:

                                                        Authorization: Bearer <token>

                                                        Example roles:

                                                        ADMIN
                                                        CLIENT
                                                        SUPPLIER
                                                        REPAIR
                                                        """)
                                )
                )

                // ============================================================
                // DEFAULT SECURITY
                // ============================================================

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEME_NAME)
                );
    }
}