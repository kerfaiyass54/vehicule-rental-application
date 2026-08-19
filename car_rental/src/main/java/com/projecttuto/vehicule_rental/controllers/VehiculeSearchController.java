package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.VehiculeResultDTO;
import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import com.projecttuto.vehicule_rental.services.VehiculeSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
@Slf4j
@Validated
public class VehiculeSearchController {

    private final VehiculeSearchService vehiculeSearchService;

    @Operation(summary = "Search vehicles")
    @GetMapping("/search")
    public ResponseEntity<Page<VehiculeResultDTO>> searchVehicles(

            @Parameter(description = "Keyword to search by vehicle name or brand")
            @RequestParam(required = false)
            String keyword,

            @Parameter(description = "Vehicle transmission type")
            @RequestParam(required = false)
            Transmission transmission,

            @Parameter(description = "Vehicle availability status")
            @RequestParam(required = false)
            VehiculeStatus status,

            @Parameter(description = "Minimum vehicle price")
            @RequestParam(required = false)
            @PositiveOrZero(message = "Minimum price must be greater than or equal to 0")
            Double minPrice,

            @Parameter(description = "Maximum vehicle price")
            @RequestParam(required = false)
            @PositiveOrZero(message = "Maximum price must be greater than or equal to 0")
            Double maxPrice,

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

        return ResponseEntity.ok(
                vehiculeSearchService.searchVehicules(
                        keyword,
                        transmission,
                        status,
                        minPrice,
                        maxPrice,
                        page,
                        size
                )
        );
    }
}