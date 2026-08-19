package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.services.BuyingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buyings")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Buying Management")
public class BuyingController {

    private final BuyingService buyingService;

    @Operation(summary = "Create a vehicle buying")
    @PostMapping
    public ResponseEntity<Buying> addBuying(
            @RequestParam
            @NotBlank(message = "Vehicle name is required")
            String vehiculeName,

            @RequestParam
            @NotBlank(message = "Client name is required")
            String clientName,

            @RequestParam
            @Min(value = 1, message = "Period must be at least 1")
            int period) {

        log.info(
                "Creating buying for vehicle '{}' and client '{}'",
                vehiculeName,
                clientName
        );

        Buying buying = buyingService.addBuying(
                vehiculeName,
                clientName,
                period
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(buying);
    }

    @Operation(summary = "Get buyings for a client")
    @GetMapping("/clients/{clientEmail}")
    public ResponseEntity<Page<Buying>> getBuyingByClient(
            @PathVariable
            @Email(message = "Invalid client email")
            String clientEmail,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            int size) {

        log.info(
                "Fetching buyings for client email: {}, page: {}, size: {}",
                clientEmail,
                page,
                size
        );

        Page<Buying> buyings = buyingService.getBuyingByClient(
                clientEmail,
                page,
                size
        );

        return ResponseEntity.ok(buyings);
    }
}