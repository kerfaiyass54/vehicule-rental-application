package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.LocationDTO;
import com.projecttuto.vehicule_rental.services.SupplierLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierLocationController {

    private final SupplierLocationService supplierLocationService;

    @GetMapping("/{supplierEmail}/locations/names")
    public ResponseEntity<List<String>> getLocationNames(
            @PathVariable String supplierEmail) {

        log.info(
                "Fetching location names for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierLocationService.getLocations(supplierEmail)
        );
    }

    @GetMapping("/{supplierEmail}/locations/countries")
    public ResponseEntity<List<String>> getCountries(
            @PathVariable String supplierEmail) {

        log.info(
                "Fetching countries for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierLocationService.getCountries(supplierEmail)
        );
    }

    @GetMapping("/{supplierEmail}/locations")
    public ResponseEntity<List<LocationDTO>> getLocations(
            @PathVariable String supplierEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching locations for supplier: {}, page: {}, size: {}",
                supplierEmail,
                page,
                size
        );

        return ResponseEntity.ok(
                supplierLocationService.getLocations(
                        supplierEmail,
                        size,
                        page
                )
        );
    }
}