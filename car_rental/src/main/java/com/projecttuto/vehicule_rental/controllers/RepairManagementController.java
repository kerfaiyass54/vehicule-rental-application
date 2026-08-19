package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.RepairAdminDTO;
import com.projecttuto.vehicule_rental.services.RepairManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repairs")
@RequiredArgsConstructor
@Slf4j
public class RepairManagementController {

    private final RepairManagementService repairManagementService;

    @GetMapping
    public ResponseEntity<Page<RepairAdminDTO>> getRepairs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching repair centers - page: {}, size: {}",
                page,
                size
        );

        return ResponseEntity.ok(
                repairManagementService.getRepairs(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairAdminDTO> getRepair(
            @PathVariable Long id) {

        log.info("Fetching repair center with id: {}", id);

        return ResponseEntity.ok(
                repairManagementService.getRepair(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairAdminDTO> updateRepair(
            @PathVariable Long id,
            @Valid @RequestBody RepairAdminDTO dto) {

        log.info("Updating repair center with id: {}", id);

        RepairAdminDTO updatedRepair =
                repairManagementService.updateRepair(id, dto);

        return ResponseEntity.ok(updatedRepair);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepair(
            @PathVariable Long id) {

        log.info("Deleting repair center with id: {}", id);

        repairManagementService.deleteRepair(id);

        return ResponseEntity.noContent().build();
    }
}