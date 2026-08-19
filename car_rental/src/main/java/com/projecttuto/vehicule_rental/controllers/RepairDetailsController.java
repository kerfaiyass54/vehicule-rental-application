package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.RepairDashboardDTO;
import com.projecttuto.vehicule_rental.dto.RepairProfileDTO;
import com.projecttuto.vehicule_rental.services.RepairDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repairs")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RepairDetailsController {

    private final RepairDetailsService repairDetailsService;

    @Operation(
            summary = "Get repair center profile",
            description = "Returns the profile information of a repair center."
    )
    @GetMapping("/{repairEmail}")
    public ResponseEntity<RepairProfileDTO> getInfo(
            @PathVariable
            @NotBlank(message = "Repair email is required")
            @Email(message = "Repair email must be valid")
            String repairEmail) {

        log.info("Fetching repair center information: {}", repairEmail);

        return ResponseEntity.ok(
                repairDetailsService.getInfo(repairEmail)
        );
    }

    @Operation(
            summary = "Update repair center location",
            description = "Updates the location assigned to a repair center."
    )
    @PutMapping("/{repairEmail}/location/{locationId}")
    public ResponseEntity<RepairProfileDTO> updateLocation(
            @PathVariable
            @NotBlank(message = "Repair email is required")
            @Email(message = "Repair email must be valid")
            String repairEmail,

            @PathVariable
            @Min(value = 1, message = "Location ID must be greater than 0")
            Long locationId) {

        log.info(
                "Updating location for repair center: {}, locationId: {}",
                repairEmail,
                locationId
        );

        RepairProfileDTO updatedProfile =
                repairDetailsService.updateLocation(
                        repairEmail,
                        locationId
                );

        return ResponseEntity.ok(updatedProfile);
    }

    @Operation(
            summary = "Get repair center dashboard",
            description = "Returns dashboard statistics for a repair center."
    )
    @GetMapping("/{repairEmail}/dashboard")
    public ResponseEntity<RepairDashboardDTO> getDashboard(
            @PathVariable
            @NotBlank(message = "Repair email is required")
            @Email(message = "Repair email must be valid")
            String repairEmail) {

        log.info(
                "Fetching dashboard for repair center: {}",
                repairEmail
        );

        return ResponseEntity.ok(
                repairDetailsService.getDashboard(repairEmail)
        );
    }
}