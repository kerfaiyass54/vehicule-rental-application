package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.AdminDTO;
import com.projecttuto.vehicule_rental.dto.AdminDashboardDTO;
import com.projecttuto.vehicule_rental.services.AdminDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Management")
public class AdminDetailsController {

    private final AdminDetailsService adminDetailsService;

    @Operation(summary = "Get admin details")
    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getDetails(
            @PathVariable
            @Positive(message = "Admin id must be positive")
            Long id) {

        log.info("Fetching admin details for id: {}", id);

        return ResponseEntity.ok(
                adminDetailsService.getDetails(id)
        );
    }

    @Operation(summary = "Update admin details")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDetails(
            @PathVariable
            @Positive(message = "Admin id must be positive")
            Long id,

            @Valid
            @RequestBody
            AdminDTO adminDTO) {

        log.info("Updating admin details for id: {}", id);

        adminDetailsService.updateDetails(adminDTO, id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get admin dashboard")
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> getDashboard() {

        log.info("Fetching admin dashboard");

        return ResponseEntity.ok(
                adminDetailsService.getDashboard()
        );
    }
}