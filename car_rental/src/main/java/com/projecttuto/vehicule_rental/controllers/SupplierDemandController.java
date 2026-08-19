package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.DemandResponseDTO;
import com.projecttuto.vehicule_rental.services.SupplierDemandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SupplierDemandController {

    private final SupplierDemandService supplierDemandService;

    @Operation(summary = "Get supplier demands")
    @GetMapping("/{supplierEmail}/demands")
    public ResponseEntity<Page<DemandResponseDTO>> getDemands(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail,

            @Parameter(description = "Page number (zero-based)")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @Parameter(description = "Number of elements per page")
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            @Max(value = 100, message = "Size must not exceed 100")
            int size) {

        log.info(
                "Fetching demands for supplier: {}, page: {}, size: {}",
                supplierEmail,
                page,
                size
        );

        return ResponseEntity.ok(
                supplierDemandService.checkDemands(
                        supplierEmail,
                        page,
                        size
                )
        );
    }

    @Operation(summary = "Approve a supplier demand")
    @PatchMapping("/demands/{demandId}/approve")
    public ResponseEntity<DemandResponseDTO> approveDemand(

            @Parameter(description = "Demand identifier")
            @PathVariable
            @NotNull(message = "Demand ID is required")
            Long demandId) {

        log.info("Approving demand with id: {}", demandId);

        return ResponseEntity.ok(
                supplierDemandService.approveDemand(demandId)
        );
    }

    @Operation(summary = "Refuse a supplier demand")
    @PatchMapping("/demands/{demandId}/refuse")
    public ResponseEntity<DemandResponseDTO> refuseDemand(

            @Parameter(description = "Demand identifier")
            @PathVariable
            @NotNull(message = "Demand ID is required")
            Long demandId) {

        log.info("Refusing demand with id: {}", demandId);

        return ResponseEntity.ok(
                supplierDemandService.refuseDemand(demandId)
        );
    }
}