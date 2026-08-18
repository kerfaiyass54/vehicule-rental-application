package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.PasswordDTO;
import com.projecttuto.vehicule_rental.dto.UpdateUserDTO;
import com.projecttuto.vehicule_rental.dto.UserDTO;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.utils.TestUtils;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class KeycloakAdminServiceImplTest {

    private Keycloak keycloak;
    private RealmResource realmResource;
    private UsersResource usersResource;
    private UserResource userResource;
    private RolesResource rolesResource;
    private RoleResource roleResource;

    private AdminRepository adminRepo;
    private ClientRepository clientRepo;
    private RepairRepository repairRepo;
    private SupplierRepository supplierRepo;

    private KeycloakAdminServiceImpl service;

    @BeforeEach
    void setup() {

        keycloak = mock(Keycloak.class);

        realmResource = mock(RealmResource.class);
        usersResource = mock(UsersResource.class);
        userResource = mock(UserResource.class);
        rolesResource = mock(RolesResource.class);
        roleResource = mock(RoleResource.class);

        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(realmResource.roles()).thenReturn(rolesResource);

        when(usersResource.get(anyString())).thenReturn(userResource);
        when(rolesResource.get(anyString())).thenReturn(roleResource);

        adminRepo = mock(AdminRepository.class);
        clientRepo = mock(ClientRepository.class);
        repairRepo = mock(RepairRepository.class);
        supplierRepo = mock(SupplierRepository.class);

        service = new KeycloakAdminServiceImpl(
                keycloak,
                adminRepo,
                clientRepo,
                supplierRepo,
                repairRepo
        );

        TestUtils.setField(service, "userRealm", "myRealm");
    }

    // ------------------------ CREATE USER -------------------------

    @Test
    void testCreateUser_success() {

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("john");
        userDTO.setEmail("john@test.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setPassword("1234");
        userDTO.setRole("client");

        Response response = mock(Response.class);

        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(201);
        when(response.getLocation()).thenReturn(URI.create("http://localhost/users/123"));

        RoleRepresentation roleRepresentation = new RoleRepresentation();

        when(roleResource.toRepresentation()).thenReturn(roleRepresentation);

        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);

        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);

        doNothing().when(roleScopeResource).add(anyList());

        service.createUser(userDTO);

        verify(usersResource).create(any(UserRepresentation.class));
        verify(userResource).resetPassword(any(CredentialRepresentation.class));
        verify(roleScopeResource).add(anyList());
    }

    // ------------------------ GET ALL USERS -------------------------

    @Test
    void testGetAllUsers() {

        UserRepresentation rep = new UserRepresentation();
        when(usersResource.list()).thenReturn(List.of(rep));

        List<UserRepresentation> result = service.getAllUsers();

        assertEquals(1, result.size());
    }

    // ------------------------ DELETE USER -------------------------

    @Test
    void testDeleteUser_client() {

        when(clientRepo.findClientByEmail("john@test.com")).thenReturn(null);

        service.deleteUser("123", "client", "john@test.com");

        verify(usersResource).delete("123");
        verify(clientRepo).delete(null);
    }

    // ------------------------ UPDATE PASSWORD -------------------------

    @Test
    void testUpdatePassword_admin() {

        PasswordDTO dto = new PasswordDTO();
        dto.setNewPassword("newPass");
        dto.setEmail("john@test.com");
        dto.setRole("admin");

        when(usersResource.get("123")).thenReturn(userResource);

        service.updatePassword("123", dto);

        verify(userResource).resetPassword(any(CredentialRepresentation.class));
        verify(adminRepo).updatePassword("john@test.com", "newPass");
    }

    // ------------------------ UPDATE USER WITHOUT PASSWORD -------------------------

    @Test
    void testUpdateUserWithoutPassword_client() {

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setEmail("old@mail.com");
        dto.setNewEmail("new@mail.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setRole("client");

        UserRepresentation rep = new UserRepresentation();

        when(usersResource.get("123")).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(rep);

        service.updateUserWithoutPassword("123", dto);

        verify(clientRepo).updateEmail("old@mail.com", "new@mail.com");
        verify(userResource).update(any(UserRepresentation.class));
    }

    // ------------------------ SYNC USER -------------------------

    @Test
    void testSyncUser_userAlreadyExists() {

        UserRepresentation existing = new UserRepresentation();

        when(usersResource.search("exist@mail.com", 0, 1))
                .thenReturn(List.of(existing));

        service.syncUserToKeycloak(
                "u",
                "f",
                "l",
                "exist@mail.com",
                "p",
                "client"
        );

        verify(usersResource, never()).create(any());
    }
}