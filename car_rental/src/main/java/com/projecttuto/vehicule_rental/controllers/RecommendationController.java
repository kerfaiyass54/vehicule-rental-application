//package com.projecttuto.vehicule_rental.controllers;
//
//import com.projecttuto.vehicule_rental.dto.RecommendationResponseDTO;
//import com.projecttuto.vehicule_rental.services.RecommendationService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.enums.ParameterIn;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.constraints.Min;
//import lombok.RequiredArgsConstructor;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/recommendations")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//@Validated
//@Tag(
//        name = "Recommendations",
//        description = "APIs for vehicle recommendations"
//)
//public class RecommendationController {
//
//    private final RecommendationService service;
//
//
//    // =========================================================
//    // GET VEHICLE RECOMMENDATIONS
//    // =========================================================
//
//    @Operation(
//            summary = "Get vehicle recommendations",
//            description = """
//                    Returns vehicle recommendations based on the specified
//                    vehicle identifier.
//                    """
//    )
//    @ApiResponses({
//
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Recommendations successfully retrieved",
//                    content = @Content(
//                            mediaType = "application/json",
//                            schema = @Schema(
//                                    implementation = RecommendationResponseDTO.class
//                            )
//                    )
//            ),
//
//            @ApiResponse(
//                    responseCode = "400",
//                    description = "Invalid vehicle ID",
//                    content = @Content
//            ),
//
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Vehicle not found",
//                    content = @Content
//            ),
//
//            @ApiResponse(
//                    responseCode = "500",
//                    description = "Internal server error",
//                    content = @Content
//            )
//    })
//    @GetMapping("/{vehicleId}")
//    public RecommendationResponseDTO getRecommendations(
//
//            @Parameter(
//                    name = "vehicleId",
//                    description = "Unique identifier of the vehicle",
//                    example = "10",
//                    required = true,
//                    in = ParameterIn.PATH
//            )
//            @PathVariable
//            @Min(
//                    value = 1,
//                    message = "Vehicle ID must be greater than 0"
//            )
//            Integer vehicleId
//    ) {
//
//        return service.getRecommendations(vehicleId);
//    }
//}