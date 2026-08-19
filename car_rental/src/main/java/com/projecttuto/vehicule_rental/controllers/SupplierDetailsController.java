package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.SupplierDashboardDTO;
import com.projecttuto.vehicule_rental.dto.SupplierDetailsDTO;
import com.projecttuto.vehicule_rental.services.SupplierDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SupplierDetailsController {

    private final SupplierDetailsService supplierDetailsService;

    @Operation(summary = "Get supplier dashboard")
    @GetMapping("/{supplierEmail}/dashboard")
    public ResponseEntity<SupplierDashboardDTO> getDashboard(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail) {

        log.info(
                "Fetching dashboard for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierDetailsService.getDashboard(supplierEmail)
        );
    }

    @Operation(summary = "Get supplier details")
    @GetMapping("/{supplierEmail}/details")
    public ResponseEntity<SupplierDetailsDTO> getDetails(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail) {

        log.info(
                "Fetching details for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierDetailsService.getDetails(supplierEmail)
        );
    }
}