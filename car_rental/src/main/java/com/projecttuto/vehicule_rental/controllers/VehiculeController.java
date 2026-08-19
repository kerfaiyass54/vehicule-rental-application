package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeUpdate;
import com.projecttuto.vehicule_rental.services.VehiculeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@Slf4j
@Validated
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @Operation(summary = "Create a vehicle")
    @PostMapping
    public ResponseEntity<VehiculeDTO> addVehicle(
            @Valid @RequestBody VehiculeDTO dto) {

        log.info(
                "Creating vehicle: {}",
                dto.getNameVehicule()
        );

        VehiculeDTO created =
                vehiculeService.addVehicule(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @Operation(summary = "Get vehicle by ID")
    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> getVehicle(

            @Parameter(description = "Vehicle identifier")
            @PathVariable
            @NotNull(message = "Vehicle ID is required")
            Long id) {

        log.info("Fetching vehicle with id: {}", id);

        return ResponseEntity.ok(
                vehiculeService.getVehiculeById(id)
        );
    }

    @Operation(summary = "Update a vehicle")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVehicle(

            @Parameter(description = "Vehicle identifier")
            @PathVariable
            @NotNull(message = "Vehicle ID is required")
            Long id,

            @Valid @RequestBody VehiculeUpdate dto) {

        log.info("Updating vehicle with id: {}", id);

        vehiculeService.updateVehicule(dto, id);

        return ResponseEntity.noContent().build();
    }
}