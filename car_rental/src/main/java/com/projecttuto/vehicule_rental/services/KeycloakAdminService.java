package com.projecttuto.vehicule_rental.services;


import com.projecttuto.vehicule_rental.dto.PasswordDTO;
import com.projecttuto.vehicule_rental.dto.UpdateUserDTO;
import com.projecttuto.vehicule_rental.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

public interface KeycloakAdminService {

    public void createUser(UserDTO userDTO);

    public List<UserRepresentation> getAllUsers();

    public void deleteUser(String userId, String role, String email);

    public void updatePassword(String userId, PasswordDTO passwordDTO);

    public List<RoleRepresentation> getAllRoles();

    public void updateUserWithoutPassword(String userId, UpdateUserDTO  updateUserDTO);

    public void addRealmRoleToUser(String userId, String roleName);



}
