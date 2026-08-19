package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.SupplierDashboardDTO;
import com.projecttuto.vehicule_rental.dto.SupplierDetailsDTO;
import com.projecttuto.vehicule_rental.services.SupplierDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierDetailsController {

    private final SupplierDetailsService supplierDetailsService;

    @GetMapping("/{supplierEmail}/dashboard")
    public ResponseEntity<SupplierDashboardDTO> getDashboard(
            @PathVariable String supplierEmail) {

        log.info(
                "Fetching dashboard for supplier: {}",
                supplierEmail
        );

        SupplierDashboardDTO dashboard =
                supplierDetailsService.getDashboard(supplierEmail);

        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/{supplierEmail}/details")
    public ResponseEntity<SupplierDetailsDTO> getDetails(
            @PathVariable String supplierEmail) {

        log.info(
                "Fetching details for supplier: {}",
                supplierEmail
        );

        SupplierDetailsDTO details =
                supplierDetailsService.getDetails(supplierEmail);

        return ResponseEntity.ok(details);
    }
}