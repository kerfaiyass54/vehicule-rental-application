package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.RepairAdminDTO;
import com.projecttuto.vehicule_rental.dto.RepairCreationDTO;
import com.projecttuto.vehicule_rental.services.RepairManagementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repairs-management")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RepairManagementController {

    private final RepairManagementService repairManagementService;

    @Operation(
            summary = "Create repair center",
            description =
                    "Creates a new repair center."
    )
    @PostMapping
    public ResponseEntity<RepairAdminDTO> createRepair(

            @Valid
            @RequestBody
            RepairCreationDTO dto

    ) {

        log.info(
                "Creating repair center: {}",
                dto.getRepairName()
        );


        RepairAdminDTO createdRepair =
                repairManagementService
                        .createRepair(dto);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRepair);

    }

    @Operation(
            summary = "Get all repair centers",
            description = "Returns a paginated list of repair centers."
    )
    @GetMapping
    public ResponseEntity<Page<RepairAdminDTO>> getRepairs(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            int size) {

        log.info(
                "Fetching repair centers - page: {}, size: {}",
                page,
                size
        );

        return ResponseEntity.ok(
                repairManagementService.getRepairs(page, size)
        );
    }

    @Operation(
            summary = "Get repair center by ID",
            description = "Returns a repair center using its unique identifier."
    )
    @GetMapping("/{id}")
    public ResponseEntity<RepairAdminDTO> getRepair(
            @PathVariable
            @Min(value = 1, message = "Repair center ID must be greater than 0")
            Long id) {

        log.info("Fetching repair center with id: {}", id);

        return ResponseEntity.ok(
                repairManagementService.getRepair(id)
        );
    }

    @Operation(
            summary = "Update repair center",
            description = "Updates the information of an existing repair center."
    )
    @PutMapping("/{id}")
    public ResponseEntity<RepairAdminDTO> updateRepair(
            @PathVariable
            @Min(value = 1, message = "Repair center ID must be greater than 0")
            Long id,

            @Valid @RequestBody RepairAdminDTO dto) {

        log.info("Updating repair center with id: {}", id);

        RepairAdminDTO updatedRepair =
                repairManagementService.updateRepair(id, dto);

        return ResponseEntity.ok(updatedRepair);
    }

    @Operation(
            summary = "Delete repair center",
            description = "Deletes an existing repair center."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepair(
            @PathVariable
            @Min(value = 1, message = "Repair center ID must be greater than 0")
            Long id) {

        log.info("Deleting repair center with id: {}", id);

        repairManagementService.deleteRepair(id);

        return ResponseEntity.noContent().build();
    }
}