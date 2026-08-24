package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.LocationDTO;
import com.projecttuto.vehicule_rental.services.ClientLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Client Location Management")
public class ClientLocationController {

    private final ClientLocationService clientLocationService;


    // ---------------------------------------------------------
    // GET CLIENT LOCATION
    // ---------------------------------------------------------

    @Operation(summary = "Get client location")
    @GetMapping("/{clientEmail}/location")
    public ResponseEntity<LocationDTO> getClientLocation(

            @PathVariable
            @Email(message = "Invalid client email")
            String clientEmail) {

        log.info(
                "Fetching location for client: {}",
                clientEmail
        );

        LocationDTO location =
                clientLocationService.getClientLocation(
                        clientEmail
                );

        return ResponseEntity.ok(location);
    }


    // ---------------------------------------------------------
    // UPDATE CLIENT LOCATION
    // ---------------------------------------------------------

    @Operation(summary = "Update client location")
    @PutMapping("/{clientEmail}/location")
    public ResponseEntity<LocationDTO> updateClientLocation(

            @PathVariable
            @Email(message = "Invalid client email")
            String clientEmail,

            @Valid
            @RequestBody
            LocationDTO locationDTO) {

        log.info(
                "Updating location for client: {}",
                clientEmail
        );

        LocationDTO updatedLocation =
                clientLocationService.updateClientLocation(
                        clientEmail,
                        locationDTO
                );

        return ResponseEntity.ok(updatedLocation);
    }
}