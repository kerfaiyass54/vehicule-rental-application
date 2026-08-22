package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.VehiculeCreation;
import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeListDTO;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import com.projecttuto.vehicule_rental.services.SupplierVehiculesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SupplierVehiculeController {

    private final SupplierVehiculesService supplierVehiculesService;

    @Operation(summary = "Get supplier vehicle count")
    @GetMapping("/{supplierEmail}/vehicles/count")
    public ResponseEntity<Integer> getSupplierVehiclesCount(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail) {

        log.info(
                "Fetching vehicle count for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getSupplierVehicules(supplierEmail)
        );
    }

    @Operation(summary = "Get vehicles by supplier")
    @GetMapping("/vehicles")
    public ResponseEntity<Page<VehiculeListDTO>> getVehicles(

            @Parameter(description = "Supplier name")
            @RequestParam
            String supplierEmail,

            @Parameter(description = "Page number (zero-based)")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @Parameter(description = "Number of vehicles per page")
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            @Max(value = 100, message = "Size must not exceed 100")
            int size) {

        log.info(
                "Fetching vehicles for supplier: {}, page: {}, size: {}",
                supplierEmail,
                page,
                size
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getVehiculeList(
                        size,
                        page,
                        supplierEmail
                )
        );
    }

    @Operation(summary = "Get total supplier vehicles")
    @GetMapping("/{supplierEmail}/vehicles/total")
    public ResponseEntity<Integer> getTotalVehicles(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail) {

        log.info(
                "Fetching total vehicles for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getTotalVehicules(supplierEmail)
        );
    }

    @Operation(summary = "Count supplier vehicles by status")
    @GetMapping("/{supplierEmail}/vehicles/count-by-status")
    public ResponseEntity<Integer> countVehiclesByStatus(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail,

            @Parameter(description = "Vehicle status")
            @RequestParam
            VehiculeStatus status) {

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

    @Operation(summary = "Get all supplier vehicles")
    @GetMapping("/{supplierEmail}/vehicles")
    public ResponseEntity<List<VehiculeDTO>> getVehiclesList(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail) {

        log.info(
                "Fetching all vehicles for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getVehiculesList(supplierEmail)
        );
    }

    @Operation(summary = "Create a vehicle for a supplier")
    @PostMapping("/{supplierEmail}/vehicles")
    public ResponseEntity<Vehicule> addVehicle(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail,

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

    @Operation(summary = "Get supplier vehicle names")
    @GetMapping("/{supplierEmail}/vehicles/names")
    public ResponseEntity<List<String>> getVehicleNames(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail) {

        log.info(
                "Fetching vehicle names for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getVehiculesNames(supplierEmail)
        );
    }

    @Operation(summary = "Get supplier vehicle IDs")
    @GetMapping("/{supplierEmail}/vehicles/ids")
    public ResponseEntity<List<Long>> getVehicleIds(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail) {

        log.info(
                "Fetching vehicle IDs for supplier: {}",
                supplierEmail
        );

        return ResponseEntity.ok(
                supplierVehiculesService.getVehiculesIds(supplierEmail)
        );
    }
}