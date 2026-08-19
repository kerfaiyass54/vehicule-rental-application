package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeUpdate;
import com.projecttuto.vehicule_rental.services.VehiculeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@Slf4j
public class VehiculeController {

    private final VehiculeService vehiculeService;

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

    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> getVehicle(
            @PathVariable Long id) {

        log.info("Fetching vehicle with id: {}", id);

        return ResponseEntity.ok(
                vehiculeService.getVehiculeById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehiculeUpdate dto) {

        log.info("Updating vehicle with id: {}", id);

        vehiculeService.updateVehicule(dto, id);

        return ResponseEntity.noContent().build();
    }
}