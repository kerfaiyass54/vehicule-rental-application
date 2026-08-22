package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.PasswordDTO;
import com.projecttuto.vehicule_rental.dto.UpdateUserDTO;
import com.projecttuto.vehicule_rental.dto.UserDTO;
import com.projecttuto.vehicule_rental.services.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminServiceImpl implements KeycloakAdminService {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-realm}")
    private String adminRealm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    @Value("${keycloak.sync.default-password}")
    private String defaultPassword;

    private Keycloak getKeycloak() {

        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(adminRealm)
                .username(adminUsername)
                .password(adminPassword)
                .clientId(clientId)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }


    // =========================================================
    // CREATE USER
    // =========================================================

    @Override
    public void createUser(UserDTO userDTO) {

        Keycloak keycloak = getKeycloak();

        try {

            UserRepresentation user =
                    new UserRepresentation();

            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setEnabled(true);
            user.setEmailVerified(false);

            CredentialRepresentation credential =
                    new CredentialRepresentation();

            credential.setType(
                    CredentialRepresentation.PASSWORD
            );

            credential.setValue(defaultPassword);

            credential.setTemporary(true);

            user.setCredentials(
                    List.of(credential)
            );

            user.setRequiredActions(
                    List.of("UPDATE_PASSWORD")
            );

            Response response =
                    keycloak.realm(realm)
                            .users()
                            .create(user);

            if (response.getStatus() != 201) {

                throw new RuntimeException(
                        "Unable to create Keycloak user. Status: "
                                + response.getStatus()
                );
            }

            String userId =
                    CreatedResponseUtil.getCreatedId(response);

            log.info(
                    "Keycloak user created: {}",
                    userDTO.getEmail()
            );

            if (userDTO.getRole() != null) {

                addRealmRoleToUser(
                        userId,
                        userDTO.getRole()
                );
            }

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // GET ALL USERS
    // =========================================================

    @Override
    public List<UserRepresentation> getAllUsers() {

        Keycloak keycloak = getKeycloak();

        try {

            return keycloak.realm(realm)
                    .users()
                    .list();

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // FIND USER BY EMAIL
    // =========================================================

    public UserRepresentation findUserByEmail(
            String email) {

        Keycloak keycloak = getKeycloak();

        try {

            List<UserRepresentation> users =
                    keycloak.realm(realm)
                            .users()
                            .searchByEmail(email, true);

            if (users == null || users.isEmpty()) {
                return null;
            }

            return users.get(0);

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // FIND USER BY USERNAME
    // =========================================================

    public UserRepresentation findUserByUsername(
            String username) {

        Keycloak keycloak = getKeycloak();

        try {

            List<UserRepresentation> users =
                    keycloak.realm(realm)
                            .users()
                            .searchByUsername(username, true);

            if (users == null || users.isEmpty()) {
                return null;
            }

            return users.get(0);

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @Override
    public void deleteUser(
            String id,
            String role,
            String email) {

        Keycloak keycloak = getKeycloak();

        try {

            keycloak.realm(realm)
                    .users()
                    .get(id)
                    .remove();

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // UPDATE PASSWORD
    // =========================================================

    @Override
    public void updatePassword(
            String id,
            PasswordDTO passwordDTO) {

        Keycloak keycloak = getKeycloak();

        try {

            CredentialRepresentation credential =
                    new CredentialRepresentation();

            credential.setType(
                    CredentialRepresentation.PASSWORD
            );

            credential.setValue(
                    passwordDTO.getNewPassword()
            );

            credential.setTemporary(false);

            keycloak.realm(realm)
                    .users()
                    .get(id)
                    .resetPassword(credential);

            /*
             * Changing the password should invalidate
             * existing sessions.
             */
            keycloak.realm(realm)
                    .users()
                    .get(id)
                    .logout();

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // UPDATE USER WITHOUT PASSWORD
    // =========================================================

    @Override
    public void updateUserWithoutPassword(
            String userID,
            UpdateUserDTO dto) {

        Keycloak keycloak = getKeycloak();

        try {

            UserRepresentation user =
                    keycloak.realm(realm)
                            .users()
                            .get(userID)
                            .toRepresentation();
            String username =  dto.getFirstName() + ' ' + dto.getLastName();

            user.setUsername(username);

            if (dto.getEmail() != null) {
                user.setEmail(dto.getEmail());
            }

            keycloak.realm(realm)
                    .users()
                    .get(userID)
                    .update(user);

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // GET ALL ROLES
    // =========================================================

    @Override
    public List<RoleRepresentation> getAllRoles() {

        Keycloak keycloak = getKeycloak();

        try {

            return keycloak.realm(realm)
                    .roles()
                    .list();

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // ADD REALM ROLE
    // =========================================================

    @Override
    public void addRealmRoleToUser(
            String userId,
            String roleName) {

        Keycloak keycloak = getKeycloak();

        try {

            RoleRepresentation role =
                    keycloak.realm(realm)
                            .roles()
                            .get(roleName)
                            .toRepresentation();

            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(
                            Collections.singletonList(role)
                    );

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // SYNCHRONIZE USER
    // =========================================================

    public void synchronizeUser(
            String email,
            String username,
            String role) {

        UserRepresentation existing =
                findUserByEmail(email);

        if (existing == null) {

            createSynchronizedUser(
                    email,
                    username,
                    role
            );

            return;
        }

        updateSynchronizedUser(
                existing,
                email,
                username,
                role
        );
    }


    // =========================================================
    // CREATE USER DURING SYNCHRONIZATION
    // =========================================================

    private void createSynchronizedUser(
            String email,
            String username,
            String role) {

        Keycloak keycloak = getKeycloak();

        try {

            UserRepresentation user =
                    new UserRepresentation();

            user.setUsername(username);
            user.setEmail(email);
            user.setEnabled(true);
            user.setEmailVerified(false);

            CredentialRepresentation credential =
                    new CredentialRepresentation();

            credential.setType(
                    CredentialRepresentation.PASSWORD
            );

            credential.setValue(defaultPassword);

            /*
             * User must change the generated password.
             */
            credential.setTemporary(true);

            user.setCredentials(
                    List.of(credential)
            );

            user.setRequiredActions(
                    List.of("UPDATE_PASSWORD")
            );

            Response response =
                    keycloak.realm(realm)
                            .users()
                            .create(user);

            if (response.getStatus() != 201) {

                throw new RuntimeException(
                        "Could not create synchronized user: "
                                + email
                );
            }

            String userId =
                    CreatedResponseUtil.getCreatedId(response);

            log.info(
                    "Created Keycloak user {}",
                    email
            );

            assignRole(
                    keycloak,
                    userId,
                    role
            );

            /*
             * Make sure there are no active sessions.
             */
            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .logout();

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // UPDATE EXISTING USER DURING SYNCHRONIZATION
    // =========================================================

    private void updateSynchronizedUser(
            UserRepresentation user,
            String email,
            String username,
            String role) {

        Keycloak keycloak = getKeycloak();

        try {

            boolean changed = false;

            if (!email.equals(user.getEmail())) {

                user.setEmail(email);
                changed = true;
            }

            if (!username.equals(user.getUsername())) {

                user.setUsername(username);
                changed = true;
            }

            if (!Boolean.TRUE.equals(user.isEnabled())) {

                user.setEnabled(true);
                changed = true;
            }

            if (changed) {

                keycloak.realm(realm)
                        .users()
                        .get(user.getId())
                        .update(user);
            }

            assignRole(
                    keycloak,
                    user.getId(),
                    role
            );

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // ASSIGN ROLE
    // =========================================================

    private void assignRole(
            Keycloak keycloak,
            String userId,
            String roleName) {

        if (roleName == null ||
                roleName.isBlank()) {

            return;
        }

        RoleRepresentation role =
                keycloak.realm(realm)
                        .roles()
                        .get(roleName)
                        .toRepresentation();

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(
                        List.of(role)
                );
    }


    // =========================================================
    // GET APPLICATION ROLE
    // =========================================================

    public String getPrimaryRole(
            String userId) {

        Keycloak keycloak = getKeycloak();

        try {

            List<RoleRepresentation> roles =
                    keycloak.realm(realm)
                            .users()
                            .get(userId)
                            .roles()
                            .realmLevel()
                            .listEffective();

            return roles.stream()
                    .map(RoleRepresentation::getName)
                    .filter(this::isApplicationRole)
                    .findFirst()
                    .orElse(null);

        } finally {

            keycloak.close();
        }
    }


    private boolean isApplicationRole(
            String role) {

        return role.equalsIgnoreCase("ADMIN")
                || role.equalsIgnoreCase("CLIENT")
                || role.equalsIgnoreCase("SUPPLIER")
                || role.equalsIgnoreCase("REPAIR");
    }


    // =========================================================
    // LOGOUT ALL SESSIONS
    // =========================================================

    public void logoutUser(String userId) {

        Keycloak keycloak = getKeycloak();

        try {

            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .logout();

            log.info(
                    "All sessions invalidated for {}",
                    userId
            );

        } finally {

            keycloak.close();
        }
    }
}