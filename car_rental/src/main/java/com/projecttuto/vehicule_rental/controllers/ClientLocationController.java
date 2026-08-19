package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.LocationDTO;
import com.projecttuto.vehicule_rental.services.ClientLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientLocationController {

    private final ClientLocationService clientLocationService;

    @PutMapping("/{clientEmail}/location")
    public ResponseEntity<LocationDTO> updateClientLocation(
            @PathVariable String clientEmail,
            @Valid @RequestBody LocationDTO locationDTO) {

        log.info("Updating location for client: {}", clientEmail);

        LocationDTO updatedLocation =
                clientLocationService.updateClientLocation(
                        clientEmail,
                        locationDTO
                );

        return ResponseEntity.ok(updatedLocation);
    }
}