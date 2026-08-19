package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.VehiculeResultDTO;
import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import com.projecttuto.vehicule_rental.services.VehiculeSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@Slf4j
public class VehiculeSearchController {

    private final VehiculeSearchService vehiculeSearchService;

    @GetMapping("/search")
    public ResponseEntity<Page<VehiculeResultDTO>> searchVehicles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Transmission transmission,
            @RequestParam(required = false) VehiculeStatus status,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Searching vehicles - keyword: {}, transmission: {}, " +
                        "status: {}, minPrice: {}, maxPrice: {}, page: {}, size: {}",
                keyword,
                transmission,
                status,
                minPrice,
                maxPrice,
                page,
                size
        );

        Page<VehiculeResultDTO> result =
                vehiculeSearchService.searchVehicules(
                        keyword,
                        transmission,
                        status,
                        minPrice,
                        maxPrice,
                        page,
                        size
                );

        return ResponseEntity.ok(result);
    }
}