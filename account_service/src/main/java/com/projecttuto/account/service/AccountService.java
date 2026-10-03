package com.projecttuto.account.service;

import com.projecttuto.account.model.*;
import com.projecttuto.account.repository.LoginSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final Keycloak keycloak;
    private final LoginSessionRepository sessions;
    @Value("${keycloak.realm}") private String realm;

    public AccountService(Keycloak keycloak, LoginSessionRepository sessions) {
        this.keycloak = keycloak; this.sessions = sessions;
    }

    public UserRepresentation current(String id) {
        return keycloak.realm(realm).users().get(id).toRepresentation();
    }
    public void update(String id, AccountUpdateRequest request) {
        UserRepresentation user = current(id);
        user.setUsername(request.username());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        keycloak.realm(realm).users().get(id).update(user);
    }
    public void password(String id, PasswordUpdateRequest request) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.newPassword());
        credential.setTemporary(false);
        keycloak.realm(realm).users().get(id).resetPassword(credential);
    }
    public void logoutAll(String id) {
        keycloak.realm(realm).users().get(id).logout();
    }
    public void saveSession(String userId, String username, String email, HttpServletRequest request) {
        String sessionId = request.getHeader("X-Session-Id");
        if (sessionId == null || sessionId.isBlank()) sessionId = userId + ":" + request.getRemoteAddr();
        if (sessions.existsBySessionId(sessionId)) return;
        LoginSessionDocument document = new LoginSessionDocument();
        document.setUserId(userId); document.setUsername(username); document.setEmail(email);
        document.setSessionId(sessionId); document.setSessionStart(Instant.now());
        document.setIpAddress(request.getRemoteAddr()); document.setUserAgent(request.getHeader("User-Agent"));
        document.setRiskScore(0); document.setSuspicious(false);
        sessions.save(document);
    }
    public Page<LoginSessionDocument> sessions(String email, int page, int size) {
        return sessions.findByEmail(email, PageRequest.of(page, size));
    }
    @KafkaListener(topics = "${account.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void provision(UserProvisioningEvent event) {
        List<UserRepresentation> found = keycloak.realm(realm).users().searchByEmail(event.email(), true);
        UserRepresentation user;
        if (found.isEmpty()) {
            user = new UserRepresentation();
            user.setEnabled(true);
            user.setCredentials(List.of(passwordCredential(event.password())));
            user.setEmail(event.email());
            user.setUsername(event.username());
            user.setFirstName(event.firstName());
            user.setLastName(event.lastName());
            String id = keycloak.realm(realm).users().create(user).getLocation().getPath().replaceAll(".*/", "");
            assignRole(id, event.role());
        } else {
            user = found.get(0);
            user.setUsername(event.username());
            user.setFirstName(event.firstName());
            user.setLastName(event.lastName());
            keycloak.realm(realm).users().get(user.getId()).update(user);
        }
    }
    private CredentialRepresentation passwordCredential(String value) {
        CredentialRepresentation c = new CredentialRepresentation();
        c.setType(CredentialRepresentation.PASSWORD); c.setValue(value); c.setTemporary(false); return c;
    }
    private void assignRole(String userId, String role) {
        RoleRepresentation realmRole = keycloak.realm(realm).roles().get(role.toUpperCase()).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(List.of(realmRole));
    }
}
