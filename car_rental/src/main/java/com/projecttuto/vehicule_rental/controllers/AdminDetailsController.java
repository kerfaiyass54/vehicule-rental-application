package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.AdminDTO;
import com.projecttuto.vehicule_rental.dto.AdminDashboardDTO;
import com.projecttuto.vehicule_rental.services.AdminDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
@Slf4j
public class AdminDetailsController {

    private final AdminDetailsService adminDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getDetails(@PathVariable Long id) {

        log.info("Fetching admin details for id: {}", id);

        return ResponseEntity.ok(
                adminDetailsService.getDetails(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDetails(
            @PathVariable Long id,
            @Valid @RequestBody AdminDTO adminDTO) {

        log.info("Updating admin details for id: {}", id);

        adminDetailsService.updateDetails(adminDTO, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> getDashboard() {

        log.info("Fetching admin dashboard");

        return ResponseEntity.ok(
                adminDetailsService.getDashboard()
        );
    }
}