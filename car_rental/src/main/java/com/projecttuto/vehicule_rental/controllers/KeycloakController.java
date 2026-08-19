package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.PasswordDTO;
import com.projecttuto.vehicule_rental.dto.UpdateUserDTO;
import com.projecttuto.vehicule_rental.dto.UserDTO;
import com.projecttuto.vehicule_rental.services.KeycloakAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/keycloak")
@CrossOrigin(origins = "*")
@Validated
@RequiredArgsConstructor
@Tag(
        name = "Keycloak Administration",
        description = "APIs for managing Keycloak users and realm roles"
)
public class KeycloakController {


    private final KeycloakAdminService keycloakService;


    // =========================================================
    // CREATE USER
    // =========================================================

    @Operation(
            summary = "Create a Keycloak user",
            description = "Creates a new user in Keycloak using the provided user information."
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully created"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user data",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<Void> createUser(
            @Valid
            @RequestBody UserDTO userDTO
    ) {

        keycloakService.createUser(userDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    // =========================================================
    // GET ALL USERS
    // =========================================================

    @Operation(
            summary = "Get all Keycloak users",
            description = "Returns all users registered in the Keycloak realm."
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Users successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UserRepresentation.class
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "204",
                    description = "No users found"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserRepresentation>> getUsers() {

        List<UserRepresentation> users =
                keycloakService.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @Operation(
            summary = "Delete a Keycloak user",
            description = """
                    Deletes a user from Keycloak.
                    
                    The user ID, role and email are required to perform
                    the deletion operation.
                    """
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "204",
                    description = "User successfully deleted"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(

            @Parameter(
                    name = "id",
                    description = "Keycloak user ID",
                    example = "f8b2a8c4-1234-4567-8901-abcdef123456",
                    required = true,
                    in = ParameterIn.QUERY
            )
            @RequestParam("id")
            @NotBlank(message = "User ID is required")
            String id,


            @Parameter(
                    name = "role",
                    description = "Role associated with the user",
                    example = "CLIENT",
                    required = true,
                    in = ParameterIn.QUERY
            )
            @RequestParam("role")
            @NotBlank(message = "Role is required")
            String role,


            @Parameter(
                    name = "email",
                    description = "Email address of the user",
                    example = "user@example.com",
                    required = true,
                    in = ParameterIn.QUERY
            )
            @RequestParam("email")
            @NotBlank(message = "Email is required")
            String email
    ) {

        keycloakService.deleteUser(
                id,
                role,
                email
        );

        return ResponseEntity
                .noContent()
                .build();
    }


    // =========================================================
    // UPDATE PASSWORD
    // =========================================================

    @Operation(
            summary = "Update user password",
            description = "Updates the password of an existing Keycloak user."
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "202",
                    description = "Password update accepted"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid password data",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(

            @Parameter(
                    name = "id",
                    description = "Keycloak user ID",
                    example = "f8b2a8c4-1234-4567-8901-abcdef123456",
                    required = true,
                    in = ParameterIn.QUERY
            )
            @RequestParam("id")
            @NotBlank(message = "User ID is required")
            String id,

            @Valid
            @RequestBody PasswordDTO passwordDTO
    ) {

        keycloakService.updatePassword(
                id,
                passwordDTO
        );

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }


    // =========================================================
    // GET ALL ROLES
    // =========================================================

    @Operation(
            summary = "Get all Keycloak roles",
            description = "Returns all realm roles available in the Keycloak realm."
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Roles successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RoleRepresentation.class
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "204",
                    description = "No roles found"
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @GetMapping("/roles")
    public ResponseEntity<List<RoleRepresentation>> getRoles() {

        List<RoleRepresentation> roles =
                keycloakService.getAllRoles();

        if (roles.isEmpty()) {
            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(roles);
    }


    // =========================================================
    // UPDATE USER
    // =========================================================

    @Operation(
            summary = "Update Keycloak user",
            description = """
                    Updates the information of an existing Keycloak user.
                    
                    The password is not modified by this operation.
                    """
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "202",
                    description = "User update accepted"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user data",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PutMapping
    public ResponseEntity<Void> updateUser(

            @Parameter(
                    name = "userID",
                    description = "Keycloak user ID",
                    example = "f8b2a8c4-1234-4567-8901-abcdef123456",
                    required = true,
                    in = ParameterIn.QUERY
            )
            @RequestParam("userID")
            @NotBlank(message = "User ID is required")
            String userID,

            @Valid
            @RequestBody UpdateUserDTO updateUserDTO
    ) {

        keycloakService.updateUserWithoutPassword(
                userID,
                updateUserDTO
        );

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }


    // =========================================================
    // ADD REALM ROLE TO USER
    // =========================================================

    @Operation(
            summary = "Add a realm role to a user",
            description = "Assigns an existing Keycloak realm role to a specific user."
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "202",
                    description = "Role assignment accepted"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "User or role not found",
                    content = @Content
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PatchMapping("/role")
    public ResponseEntity<Void> addRealmRoleToUser(

            @Parameter(
                    name = "userId",
                    description = "Keycloak user ID",
                    example = "f8b2a8c4-1234-4567-8901-abcdef123456",
                    required = true,
                    in = ParameterIn.QUERY
            )
            @RequestParam("userId")
            @NotBlank(message = "User ID is required")
            String userId,


            @Parameter(
                    name = "roleName",
                    description = "Realm role to assign",
                    example = "ADMIN",
                    required = true,
                    in = ParameterIn.QUERY
            )
            @RequestParam("roleName")
            @NotBlank(message = "Role name is required")
            String roleName
    ) {

        keycloakService.addRealmRoleToUser(
                userId,
                roleName
        );

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }
}