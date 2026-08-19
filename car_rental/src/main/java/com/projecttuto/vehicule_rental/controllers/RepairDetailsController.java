package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.RepairDashboardDTO;
import com.projecttuto.vehicule_rental.dto.RepairProfileDTO;
import com.projecttuto.vehicule_rental.services.RepairDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repairs")
@RequiredArgsConstructor
@Slf4j
public class RepairDetailsController {

    private final RepairDetailsService repairDetailsService;

    @GetMapping("/{repairEmail}")
    public ResponseEntity<RepairProfileDTO> getInfo(
            @PathVariable String repairEmail) {

        log.info(
                "Fetching repair center information: {}",
                repairEmail
        );

        return ResponseEntity.ok(
                repairDetailsService.getInfo(repairEmail)
        );
    }

    @PutMapping("/{repairEmail}/location/{locationId}")
    public ResponseEntity<RepairProfileDTO> updateLocation(
            @PathVariable String repairEmail,
            @PathVariable Long locationId) {

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

    @GetMapping("/{repairEmail}/dashboard")
    public ResponseEntity<RepairDashboardDTO> getDashboard(
            @PathVariable String repairEmail) {

        log.info(
                "Fetching dashboard for repair center: {}",
                repairEmail
        );

        return ResponseEntity.ok(
                repairDetailsService.getDashboard(repairEmail)
        );
    }
}