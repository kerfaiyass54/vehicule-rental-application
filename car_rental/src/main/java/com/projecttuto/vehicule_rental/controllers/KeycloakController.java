package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.dto.PasswordDTO;
import com.projecttuto.vehicule_rental.dto.UpdateUserDTO;
import com.projecttuto.vehicule_rental.dto.UserDTO;
import com.projecttuto.vehicule_rental.services.KeycloakAdminService;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;


@RestController
@RequestMapping("/keycloak")
@CrossOrigin("*")
public class KeycloakController {


    private final KeycloakAdminService keycloakService;

    public KeycloakController(KeycloakAdminService keycloakService) {
        this.keycloakService = keycloakService;
    }


    @PostMapping("/")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        keycloakService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserRepresentation>> getUsers() {
        List<UserRepresentation> users = keycloakService.getAllUsers();
        if(users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteUser(@RequestParam String id, @RequestParam String role,@RequestParam String email) {
        keycloakService.deleteUser(id,role,email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestParam String id,@RequestBody PasswordDTO passwordDTO) {
        keycloakService.updatePassword(id, passwordDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }



    @GetMapping("/roles")
    public ResponseEntity<List<RoleRepresentation>> getRoles() {
        List<RoleRepresentation> roles = keycloakService.getAllRoles();
        if(roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }


    @PutMapping("/")
    public ResponseEntity<Void> updateUser(@RequestParam String userID, @RequestBody UpdateUserDTO updateUserDTO)
    {
        keycloakService.updateUserWithoutPassword(userID, updateUserDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PatchMapping("/role")
    public ResponseEntity<Void> addRealmRoleToUser(@RequestParam String userId,@RequestParam String roleName){
        keycloakService.addRealmRoleToUser(userId,roleName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }





}
