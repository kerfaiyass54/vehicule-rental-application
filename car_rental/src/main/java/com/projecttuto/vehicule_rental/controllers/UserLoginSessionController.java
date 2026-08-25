//package com.projecttuto.vehicule_rental.controllers;
//
//import com.projecttuto.vehicule_rental.dto.SessionDTO;
//import com.projecttuto.vehicule_rental.services.UserLoginSessionService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.enums.ParameterIn;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/sessions")
//@CrossOrigin("*")
//@Validated
//@RequiredArgsConstructor
//@Tag(
//        name = "User Login Sessions",
//        description = "APIs for managing and retrieving user login sessions"
//)
//public class UserLoginSessionController {
//
//    private final UserLoginSessionService service;
//
//
//    // =========================================================
//    // SAVE SESSION
//    // =========================================================
//
//    @Operation(
//            summary = "Save a user login session",
//            description = """
//                    Creates and stores a new login session for the authenticated user.
//
//                    The user information is extracted from the JWT token and
//                    the client request. The system also resolves the client's
//                    geographical location and performs AI-based behavior analysis.
//                    """
//    )
//    @ApiResponses({
//
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Login session successfully saved"
//            ),
//
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Invalid request",
//                    content = @Content
//            ),
//
//            @ApiResponse(
//                    responseCode = "401",
//                    description = "Unauthorized",
//                    content = @Content
//            ),
//
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Internal server error",
//                    content = @Content
//            )
//    })
//    @PostMapping
//    public ResponseEntity<Void> saveSession(
//            HttpServletRequest request
//    ) {
//
//        service.saveSession(request);
//
//        return ResponseEntity.ok().build();
//    }
//
//
//    // =========================================================
//    // GET USER SESSIONS
//    // =========================================================
//
//    @Operation(
//            summary = "Get user login sessions",
//            description = """
//                    Returns a paginated list of login sessions belonging
//                    to a specific user identified by their email address.
//                    """
//    )
//    @ApiResponses({
//
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Sessions successfully retrieved",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(
//                                    implementation = Page.class
//                            )
//                    )
//            ),
//
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Invalid parameters",
//                    content = @Content
//            ),
//
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "No sessions found",
//                    content = @Content
//            )
//    })
//    @GetMapping
//    public ResponseEntity<Page<SessionDTO>> findAllUserLoginSessionsByEmailPage(
//
//            @Parameter(
//                    name = "page",
//                    description = "Page number. Starts from 0.",
//                    example = "0",
//                    in = ParameterIn.QUERY
//            )
//            @RequestParam(
//                    value = "page",
//                    defaultValue = "0"
//            )
//            @Min(
//                    value = 0,
//                    message = "Page must be greater than or equal to 0"
//            )
//            int page,
//
//
//            @Parameter(
//                    name = "size",
//                    description = "Number of sessions per page. Must be between 1 and 100.",
//                    example = "5",
//                    in = ParameterIn.QUERY
//            )
//            @RequestParam(
//                    value = "size",
//                    defaultValue = "5"
//            )
//            @Min(
//                    value = 1,
//                    message = "Size must be greater than 0"
//            )
//            int size,
//
//
//            @Parameter(
//                    name = "email",
//                    description = "Email address of the user",
//                    example = "user@example.com",
//                    required = true,
//                    in = ParameterIn.QUERY
//            )
//            @RequestParam("email")
//            @NotBlank(
//                    message = "Email is required"
//            )
//            @Email(
//                    message = "Email must be valid"
//            )
//            String email
//    ) {
//
//        Page<SessionDTO> sessions =
//                service.findAllUseLoginSessionsByEmailPage(
//                        email,
//                        page,
//                        size
//                );
//
//        return ResponseEntity.ok(sessions);
//    }
//
//
//    // =========================================================
//    // GET SESSION BY ID
//    // =========================================================
//
//    @Operation(
//            summary = "Get a login session by ID",
//            description = "Returns detailed information about a specific user login session."
//    )
//    @ApiResponses({
//
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Session successfully retrieved",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(
//                                    implementation = SessionDTO.class
//                            )
//                    )
//            ),
//
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Invalid session ID",
//                    content = @Content
//            ),
//
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Session not found",
//                    content = @Content
//            )
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<SessionDTO> findSessionById(
//
//            @Parameter(
//                    name = "id",
//                    description = "Unique identifier of the login session",
//                    example = "65f1a2b3c4d5e6",
//                    required = true,
//                    in = ParameterIn.PATH
//            )
//            @PathVariable("id")
//            @NotBlank(
//                    message = "Session ID is required"
//            )
//            @Size(
//                    min = 1,
//                    max = 100,
//                    message = "Session ID must contain between 1 and 100 characters"
//            )
//            String id
//    ) {
//
//        SessionDTO sessionDTO = service.getSession(id);
//
//        return ResponseEntity.ok(sessionDTO);
//    }
//}