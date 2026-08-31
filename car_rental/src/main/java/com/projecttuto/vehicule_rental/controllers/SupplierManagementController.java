package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.SupplierAdminDTO;
import com.projecttuto.vehicule_rental.services.SupplierManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/suppliers")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SupplierManagementController {

    private final SupplierManagementService supplierManagementService;

    @Operation(
            summary = "Create supplier"
    )
    @PostMapping
    public ResponseEntity<SupplierAdminDTO> createSupplier(

            @Valid
            @RequestBody
            SupplierAdminDTO dto) {

        log.info(
                "Creating supplier: {}",
                dto.getSuppName()
        );

        SupplierAdminDTO createdSupplier =
                supplierManagementService.createSupplier(
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdSupplier);
    }

    @Operation(summary = "Get all suppliers")
    @GetMapping
    public ResponseEntity<Page<SupplierAdminDTO>> getSuppliers(

            @Parameter(description = "Page number (zero-based)")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @Parameter(description = "Number of suppliers per page")
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            @Max(value = 100, message = "Size must not exceed 100")
            int size) {

        log.info(
                "Fetching suppliers - page: {}, size: {}",
                page,
                size
        );

        return ResponseEntity.ok(
                supplierManagementService.getSuppliers(page, size)
        );
    }

    @Operation(summary = "Get supplier by ID")
    @GetMapping("/{id}")
    public ResponseEntity<SupplierAdminDTO> getSupplier(

            @Parameter(description = "Supplier identifier")
            @PathVariable
            @NotNull(message = "Supplier ID is required")
            Long id) {

        log.info("Fetching supplier with id: {}", id);

        return ResponseEntity.ok(
                supplierManagementService.getSupplier(id)
        );
    }

    @Operation(summary = "Update supplier")
    @PutMapping("/{id}")
    public ResponseEntity<SupplierAdminDTO> updateSupplier(

            @Parameter(description = "Supplier identifier")
            @PathVariable
            @NotNull(message = "Supplier ID is required")
            Long id,

            @Valid @RequestBody SupplierAdminDTO dto) {

        log.info("Updating supplier with id: {}", id);

        return ResponseEntity.ok(
                supplierManagementService.updateSupplier(id, dto)
        );
    }

    @Operation(summary = "Delete supplier")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(

            @Parameter(description = "Supplier identifier")
            @PathVariable
            @NotNull(message = "Supplier ID is required")
            Long id) {

        log.info("Deleting supplier with id: {}", id);

        supplierManagementService.deleteSupplier(id);

        return ResponseEntity.noContent().build();
    }
}