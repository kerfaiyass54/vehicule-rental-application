package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.LocationAdminDTO;
import com.projecttuto.vehicule_rental.services.LocationManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Location Management")
public class LocationManagementController {

    private final LocationManagementService locationManagementService;

    @Operation(summary = "Get all location names")
    @GetMapping("/names")
    public ResponseEntity<List<String>> getLocationsNames() {

        log.info("Fetching all location names");

        return ResponseEntity.ok(
                locationManagementService.getLocationsNames()
        );
    }

    @Operation(summary = "Get all countries")
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountries() {

        log.info("Fetching all countries");

        return ResponseEntity.ok(
                locationManagementService.getCountries()
        );
    }

    @Operation(summary = "Get cities by country")
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCitiesByCountry(
            @RequestParam
            @NotBlank(message = "Country is required")
            String country) {

        log.info("Fetching cities for country: {}", country);

        return ResponseEntity.ok(
                locationManagementService.getCitiesByCountry(country)
        );
    }

    @Operation(summary = "Create a location")
    @PostMapping
    public ResponseEntity<LocationAdminDTO> createLocation(
            @Valid @RequestBody LocationAdminDTO dto) {

        log.info("Creating location: {}", dto.getName());

        LocationAdminDTO createdLocation =
                locationManagementService.createLocation(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdLocation);
    }

    @Operation(summary = "Get all locations")
    @GetMapping
    public ResponseEntity<Page<LocationAdminDTO>> getLocations(
            @RequestParam(defaultValue = "0")
            @Min(
                    value = 0,
                    message = "Page must be greater than or equal to 0"
            )
            int page,

            @RequestParam(defaultValue = "10")
            @Min(
                    value = 1,
                    message = "Size must be greater than 0"
            )
            int size) {

        log.info(
                "Fetching locations - page: {}, size: {}",
                page,
                size
        );

        return ResponseEntity.ok(
                locationManagementService.getLocations(page, size)
        );
    }

    @Operation(summary = "Get location by ID")
    @GetMapping("/{id}")
    public ResponseEntity<LocationAdminDTO> getLocation(
            @PathVariable
            @Positive(message = "Location id must be positive")
            Long id) {

        log.info("Fetching location with id: {}", id);

        return ResponseEntity.ok(
                locationManagementService.getLocation(id)
        );
    }

    @Operation(summary = "Update location")
    @PutMapping("/{id}")
    public ResponseEntity<LocationAdminDTO> updateLocation(
            @PathVariable
            @Positive(message = "Location id must be positive")
            Long id,

            @Valid @RequestBody LocationAdminDTO dto) {

        log.info("Updating location with id: {}", id);

        LocationAdminDTO updatedLocation =
                locationManagementService.updateLocation(id, dto);

        return ResponseEntity.ok(updatedLocation);
    }

    @Operation(summary = "Delete location")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable
            @Positive(message = "Location id must be positive")
            Long id) {

        log.info("Deleting location with id: {}", id);

        locationManagementService.deleteLocation(id);

        return ResponseEntity.noContent().build();
    }
}