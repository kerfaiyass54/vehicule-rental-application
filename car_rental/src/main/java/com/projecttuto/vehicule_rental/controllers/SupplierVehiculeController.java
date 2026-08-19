package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.VehiculeCreation;
import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeListDTO;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import com.projecttuto.vehicule_rental.services.SupplierVehiculesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierVehiculeController {

    private final SupplierVehiculesService supplierVehiculesService;

    /**
     * Get the total number of vehicles belonging to a supplier.
     */
    @GetMapping("/{supplierEmail}/vehicles/count")
    public ResponseEntity<Integer> getSupplierVehiclesCount(
            @PathVariable String supplierEmail) {

        log.info(
                "Fetching vehicle count for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getSupplierVehicules(supplierEmail)
        );
    }

    /**
     * Get paginated vehicles by supplier name.
     */
    @GetMapping("/vehicles")
    public ResponseEntity<Page<VehiculeListDTO>> getVehicles(
            @RequestParam String supplierName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching vehicles for supplier: {}, page: {}, size: {}",
                supplierName,
                page,
                size
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getVehiculeList(
                        size,
                        page,
                        supplierName
                )
        );
    }

    /**
     * Get total number of vehicles.
     */
    @GetMapping("/{supplierEmail}/vehicles/total")
    public ResponseEntity<Integer> getTotalVehicles(
            @PathVariable String supplierEmail) {

        log.info(
                "Fetching total vehicles for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getTotalVehicules(supplierEmail)
        );
    }

    /**
     * Count vehicles by status.
     */
    @GetMapping("/{supplierEmail}/vehicles/count-by-status")
    public ResponseEntity<Integer> countVehiclesByStatus(
            @PathVariable String supplierEmail,
            @RequestParam VehiculeStatus status) {

        log.info(
                "Counting vehicles for supplier: {} with status: {}",
                supplierEmail,
                status
        );

        return ResponseEntity.ok(
                supplierVehiculesService
                        .countBySupplierEmailAndVehiculeStatus(
                                supplierEmail,
                                status
                        )
        );
    }

    /**
     * Get all vehicles of a supplier.
     */
    @GetMapping("/{supplierEmail}/vehicles")
    public ResponseEntity<List<VehiculeDTO>> getVehiclesList(
            @PathVariable String supplierEmail) {

        log.info(
                "Fetching all vehicles for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getVehiculesList(supplierEmail)
        );
    }

    /**
     * Create a new vehicle for a supplier.
     */
    @PostMapping("/{supplierEmail}/vehicles")
    public ResponseEntity<Vehicule> addVehicle(
            @PathVariable String supplierEmail,
            @Valid @RequestBody VehiculeCreation vehiculeCreation) {

        log.info(
                "Creating vehicle for supplier: {}",
                supplierEmail
        );

        Vehicule vehicle =
                supplierVehiculesService.addVehiculeNew(
                        vehiculeCreation,
                        supplierEmail
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicle);
    }

    /**
     * Get vehicle names belonging to a supplier.
     */
    @GetMapping("/{supplierEmail}/vehicles/names")
    public ResponseEntity<List<String>> getVehicleNames(
            @PathVariable String supplierEmail) {

        log.info(
                "Fetching vehicle names for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getVehiculesNames(supplierEmail)
        );
    }

    /**
     * Get vehicle IDs belonging to a supplier.
     */
    @GetMapping("/{supplierEmail}/vehicles/ids")
    public ResponseEntity<List<Long>> getVehicleIds(
            @PathVariable String supplierEmail) {

        log.info(
                "Fetching vehicle IDs for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getVehiculesIds(supplierEmail)
        );
    }
}