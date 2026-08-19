package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.services.AddressLocationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressLocationController {

    private final AddressLocationService addressLocationService;

    @Operation(
            summary = "Get number of addresses by location"
    )
    @GetMapping("/location/total")
    public ResponseEntity<Integer> getAddressesPerLocation(
            @RequestParam
            @NotBlank(message = "Location name is required")
            @Size(max = 100, message = "Location name must not exceed 100 characters")
            String locationName) {

        log.info(
                "Request to get address count for location: {}",
                locationName
        );

        return ResponseEntity.ok(
                addressLocationService.getAddressesPerLocation(locationName)
        );
    }
}