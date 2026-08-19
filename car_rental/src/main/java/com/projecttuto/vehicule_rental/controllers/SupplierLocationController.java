package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.LocationDTO;
import com.projecttuto.vehicule_rental.services.SupplierLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SupplierLocationController {

    private final SupplierLocationService supplierLocationService;

    @Operation(summary = "Get supplier location names")
    @GetMapping("/{supplierEmail}/locations/names")
    public ResponseEntity<List<String>> getLocationNames(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail) {

        log.info(
                "Fetching location names for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierLocationService.getLocations(supplierEmail)
        );
    }

    @Operation(summary = "Get supplier countries")
    @GetMapping("/{supplierEmail}/locations/countries")
    public ResponseEntity<List<String>> getCountries(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail) {

        log.info(
                "Fetching countries for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierLocationService.getCountries(supplierEmail)
        );
    }

    @Operation(summary = "Get supplier locations")
    @GetMapping("/{supplierEmail}/locations")
    public ResponseEntity<List<LocationDTO>> getLocations(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail,

            @Parameter(description = "Page number (zero-based)")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @Parameter(description = "Number of locations per page")
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            @Max(value = 100, message = "Size must not exceed 100")
            int size) {

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