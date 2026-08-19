package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.services.AddressLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressLocationController {

    private final AddressLocationService addressLocationService;

    @GetMapping("/count-by-location")
    public ResponseEntity<Integer> getAddressesPerLocation(
            @RequestParam String locationName) {

        log.info("Request to get address count for location: {}", locationName);

        int count = addressLocationService.getAddressesPerLocation(locationName);

        return ResponseEntity.ok(count);
    }
}