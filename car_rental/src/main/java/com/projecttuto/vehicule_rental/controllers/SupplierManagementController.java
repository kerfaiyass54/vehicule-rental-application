package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.SupplierAdminDTO;
import com.projecttuto.vehicule_rental.services.SupplierManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierManagementController {

    private final SupplierManagementService supplierManagementService;

    @GetMapping
    public ResponseEntity<Page<SupplierAdminDTO>> getSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching suppliers - page: {}, size: {}",
                page,
                size
        );

        return ResponseEntity.ok(
                supplierManagementService.getSuppliers(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierAdminDTO> getSupplier(
            @PathVariable Long id) {

        log.info("Fetching supplier with id: {}", id);

        return ResponseEntity.ok(
                supplierManagementService.getSupplier(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierAdminDTO> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierAdminDTO dto) {

        log.info("Updating supplier with id: {}", id);

        SupplierAdminDTO updatedSupplier =
                supplierManagementService.updateSupplier(id, dto);

        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(
            @PathVariable Long id) {

        log.info("Deleting supplier with id: {}", id);

        supplierManagementService.deleteSupplier(id);

        return ResponseEntity.noContent().build();
    }
}