package com.projecttuto.account.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfiguration {
    @Bean
    Keycloak keycloak(
            @Value("${keycloak.server-url}") String serverUrl,
            @Value("${keycloak.admin-realm}") String realm,
            @Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.admin-username}") String username,
            @Value("${keycloak.admin-password}") String password) {
        return KeycloakBuilder.builder().serverUrl(serverUrl).realm(realm)
                .clientId(clientId).username(username).password(password)
                .grantType(OAuth2Constants.PASSWORD).build();
    }
}
