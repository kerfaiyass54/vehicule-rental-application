package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.OwnedVehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeSearchDTO;
import com.projecttuto.vehicule_rental.services.ClientVehiculeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Client Vehicle Management")
public class ClientVehiculeController {

    private final ClientVehiculeService clientVehiculeService;

    @Operation(
            summary = "Get supplier vehicles",
            description = "Returns the vehicles belonging to a supplier with pagination."
    )
    @GetMapping("/{supplierId}/vehicules")
    public ResponseEntity<Page<VehiculeSearchDTO>> getSupplierVehicules(


            @PathVariable
            Long supplierId,

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
            @Max(
                    value = 100,
                    message = "Size must not exceed 100"
            )
            int size) {

        log.info(
                "Fetching vehicles for supplier: {}, page: {}, size: {}",
                supplierId,
                page,
                size
        );

        return ResponseEntity.ok(
                clientVehiculeService.getSupplierVehicules(
                        supplierId,
                        size,
                        page
                )
        );
    }

    @Operation(summary = "Get vehicles owned by a client")
    @GetMapping("/{clientEmail}/vehicles")
    public ResponseEntity<Page<OwnedVehiculeDTO>> getOwnedVehicules(
            @PathVariable
            @Email(message = "Invalid client email")
            String clientEmail,

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
                "Fetching owned vehicles for client: {}, page: {}, size: {}",
                clientEmail,
                page,
                size
        );

        Page<OwnedVehiculeDTO> vehicles =
                clientVehiculeService.getOwnedVehicules(
                        clientEmail,
                        page,
                        size
                );

        return ResponseEntity.ok(vehicles);
    }

    // ---------------------------------------------------------
// GET VEHICLE TOTAL PRICE
// ---------------------------------------------------------

    @GetMapping("/total-price")
    @Operation(
            summary = "Get vehicle total price",
            description = "Returns the vehicle price after applying the specified reduction."
    )
    public ResponseEntity<Double> getVehiculeTotalPrice(
            @RequestParam Long vehiculeId,
            @RequestParam Double reduction
    ) {

        return ResponseEntity.ok(
                clientVehiculeService.getVehiculeTotalPrice(
                        vehiculeId,
                        reduction
                )
        );
    }



}