package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.LocationAdminDTO;
import com.projecttuto.vehicule_rental.services.LocationManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationManagementController {

    private final LocationManagementService locationManagementService;

    @GetMapping("/names")
    public ResponseEntity<List<String>> getLocationsNames() {

        log.info("Fetching all location names");

        return ResponseEntity.ok(
                locationManagementService.getLocationsNames()
        );
    }

    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountries() {

        log.info("Fetching all countries");

        return ResponseEntity.ok(
                locationManagementService.getCountries()
        );
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCitiesByCountry(
            @RequestParam String country) {

        log.info("Fetching cities for country: {}", country);

        return ResponseEntity.ok(
                locationManagementService.getCitiesByCountry(country)
        );
    }

    @PostMapping
    public ResponseEntity<LocationAdminDTO> createLocation(
            @Valid @RequestBody LocationAdminDTO dto) {

        log.info(
                "Creating location: {}",
                dto.getName()
        );

        LocationAdminDTO createdLocation =
                locationManagementService.createLocation(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdLocation);
    }

    @GetMapping
    public ResponseEntity<Page<LocationAdminDTO>> getLocations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching locations - page: {}, size: {}",
                page,
                size
        );

        return ResponseEntity.ok(
                locationManagementService.getLocations(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationAdminDTO> getLocation(
            @PathVariable Long id) {

        log.info("Fetching location with id: {}", id);

        return ResponseEntity.ok(
                locationManagementService.getLocation(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationAdminDTO> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationAdminDTO dto) {

        log.info("Updating location with id: {}", id);

        LocationAdminDTO updatedLocation =
                locationManagementService.updateLocation(id, dto);

        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable Long id) {

        log.info("Deleting location with id: {}", id);

        locationManagementService.deleteLocation(id);

        return ResponseEntity.noContent().build();
    }
}