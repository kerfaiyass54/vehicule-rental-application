package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.PasswordDTO;
import com.projecttuto.vehicule_rental.dto.UpdateUserDTO;
import com.projecttuto.vehicule_rental.dto.UserDTO;
import com.projecttuto.vehicule_rental.services.KeycloakAdminService;
import jakarta.ws.rs.core.Response;
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

    /*
     * Principal/default password for synchronized users.
     *
     * application.properties:
     *
     * keycloak.sync.default-password=123456
     */
    @Value("${keycloak.sync.default-password}")
    private String defaultPassword;


    // =========================================================
    // KEYCLOAK CONNECTION
    // =========================================================

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

            /*
             * Principal/default password.
             */
            credential.setValue(defaultPassword);

            credential.setTemporary(false);

            user.setCredentials(
                    List.of(credential)
            );

            Response response =
                    keycloak.realm(realm)
                            .users()
                            .create(user);

            try {

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

                response.close();
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

        if (email == null || email.isBlank()) {
            return null;
        }

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

        if (username == null || username.isBlank()) {
            return null;
        }

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

            log.info(
                    "Deleted Keycloak user: {}",
                    email
            );

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
             * Logout all existing sessions.
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

            String username =
                    dto.getFirstName()
                            + " "
                            + dto.getLastName();

            user.setUsername(username);

            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());

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

            assignRole(
                    keycloak,
                    userId,
                    roleName
            );

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // SYNCHRONIZE DATABASE USER -> KEYCLOAK
    // =========================================================

    public void syncUserToKeycloak(
            String username,
            String firstName,
            String lastName,
            String email,
            String password,
            String role) {

        if (email == null || email.isBlank()) {

            log.warn(
                    "Cannot synchronize Keycloak user: email is empty"
            );

            return;
        }

        Keycloak keycloak = getKeycloak();

        try {

            List<UserRepresentation> users =
                    keycloak.realm(realm)
                            .users()
                            .searchByEmail(email, true);

            /*
             * User already exists.
             */
            if (users != null && !users.isEmpty()) {

                UserRepresentation existing =
                        users.get(0);

                log.info(
                        "Keycloak user already exists: {}",
                        email
                );

                updateSynchronizedUser(
                        keycloak,
                        existing,
                        username,
                        firstName,
                        lastName,
                        email,
                        role
                );

                return;
            }


            /*
             * Create new user.
             */
            UserRepresentation user =
                    new UserRepresentation();

            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);

            user.setEnabled(true);
            user.setEmailVerified(false);


            /*
             * IMPORTANT:
             *
             * The principal password is always:
             *
             * 123456
             *
             * from:
             *
             * keycloak.sync.default-password=123456
             *
             * We intentionally do NOT use the database
             * password here.
             */
            CredentialRepresentation credential =
                    new CredentialRepresentation();

            credential.setType(
                    CredentialRepresentation.PASSWORD
            );

            credential.setValue(
                    defaultPassword
            );

            credential.setTemporary(false);

            user.setCredentials(
                    List.of(credential)
            );


            Response response =
                    keycloak.realm(realm)
                            .users()
                            .create(user);

            try {

                if (response.getStatus() != 201) {

                    log.error(
                            "Failed to create Keycloak user {}. Status: {}",
                            email,
                            response.getStatus()
                    );

                    return;
                }

                String userId =
                        CreatedResponseUtil
                                .getCreatedId(response);


                /*
                 * Assign lowercase role.
                 *
                 * client
                 * supplier
                 * repair
                 * admin
                 */
                assignRole(
                        keycloak,
                        userId,
                        role
                );

                log.info(
                        "Keycloak user synchronized: {} | role={}",
                        email,
                        normalizeRole(role)
                );

            } finally {

                response.close();
            }

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

        if (email == null || email.isBlank()) {
            return;
        }

        Keycloak keycloak = getKeycloak();

        try {

            List<UserRepresentation> users =
                    keycloak.realm(realm)
                            .users()
                            .searchByEmail(email, true);

            /*
             * USER DOES NOT EXIST
             */
            if (users == null || users.isEmpty()) {

                createSynchronizedUser(
                        keycloak,
                        email,
                        username,
                        role
                );

                return;
            }


            /*
             * USER ALREADY EXISTS
             */
            UserRepresentation existing =
                    users.get(0);

            updateSynchronizedUser(
                    keycloak,
                    existing,
                    username,
                    null,
                    null,
                    email,
                    role
            );

        } finally {

            keycloak.close();
        }
    }


    // =========================================================
    // CREATE SYNCHRONIZED USER
    // =========================================================

    private void createSynchronizedUser(
            Keycloak keycloak,
            String email,
            String username,
            String role) {

        UserRepresentation user =
                new UserRepresentation();

        user.setUsername(username);
        user.setEmail(email);

        user.setEnabled(true);
        user.setEmailVerified(false);


        /*
         * Principal password = 123456.
         */
        CredentialRepresentation credential =
                new CredentialRepresentation();

        credential.setType(
                CredentialRepresentation.PASSWORD
        );

        credential.setValue(
                defaultPassword
        );

        credential.setTemporary(false);

        user.setCredentials(
                List.of(credential)
        );


        Response response =
                keycloak.realm(realm)
                        .users()
                        .create(user);

        try {

            if (response.getStatus() != 201) {

                throw new RuntimeException(
                        "Could not create synchronized user: "
                                + email
                                + " | status="
                                + response.getStatus()
                );
            }

            String userId =
                    CreatedResponseUtil
                            .getCreatedId(response);

            assignRole(
                    keycloak,
                    userId,
                    role
            );

            log.info(
                    "Created Keycloak user: {} | role={}",
                    email,
                    normalizeRole(role)
            );

        } finally {

            response.close();
        }
    }


    // =========================================================
    // UPDATE EXISTING SYNCHRONIZED USER
    // =========================================================

    private void updateSynchronizedUser(
            Keycloak keycloak,
            UserRepresentation user,
            String username,
            String firstName,
            String lastName,
            String email,
            String role) {

        boolean changed = false;

        if (username != null &&
                !username.equals(user.getUsername())) {

            user.setUsername(username);
            changed = true;
        }

        if (firstName != null &&
                !firstName.equals(user.getFirstName())) {

            user.setFirstName(firstName);
            changed = true;
        }

        if (lastName != null &&
                !lastName.equals(user.getLastName())) {

            user.setLastName(lastName);
            changed = true;
        }

        if (email != null &&
                !email.equals(user.getEmail())) {

            user.setEmail(email);
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

            log.info(
                    "Updated Keycloak user: {}",
                    email
            );
        }


        /*
         * Always make sure the correct application
         * role is assigned.
         */
        assignRole(
                keycloak,
                user.getId(),
                role
        );
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

            log.warn(
                    "No role provided for Keycloak user {}",
                    userId
            );

            return;
        }

        /*
         * Your Keycloak roles are lowercase.
         */
        String role =
                normalizeRole(roleName);


        RoleRepresentation roleRepresentation =
                keycloak.realm(realm)
                        .roles()
                        .get(role)
                        .toRepresentation();


        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(
                        List.of(roleRepresentation)
                );


        log.info(
                "Assigned Keycloak role '{}' to user {}",
                role,
                userId
        );
    }


    // =========================================================
    // NORMALIZE ROLE
    // =========================================================

    private String normalizeRole(
            String role) {

        if (role == null) {
            return null;
        }

        return role
                .trim()
                .toLowerCase();
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


    // =========================================================
    // CHECK APPLICATION ROLE
    // =========================================================

    private boolean isApplicationRole(
            String role) {

        if (role == null) {
            return false;
        }

        return role.equalsIgnoreCase("admin")
                || role.equalsIgnoreCase("client")
                || role.equalsIgnoreCase("supplier")
                || role.equalsIgnoreCase("repair");
    }


    // =========================================================
    // LOGOUT ALL SESSIONS
    // =========================================================

    public void logoutUser(
            String userId) {

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