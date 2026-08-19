package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.SubscriptionResponseDTO;
import com.projecttuto.vehicule_rental.services.SupplierSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SupplierSubscriptionController {

    private final SupplierSubscriptionService supplierSubscriptionService;

    @Operation(summary = "Get supplier subscriptions")
    @GetMapping("/{supplierEmail}/subscriptions")
    public ResponseEntity<Page<SubscriptionResponseDTO>> getSubscriptions(

            @Parameter(description = "Supplier email")
            @PathVariable
            @Email(message = "Invalid supplier email")
            String supplierEmail,

            @Parameter(description = "Page number (zero-based)")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @Parameter(description = "Number of subscriptions per page")
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            @Max(value = 100, message = "Size must not exceed 100")
            int size) {

        log.info(
                "Fetching subscriptions for supplier: {}, page: {}, size: {}",
                supplierEmail,
                page,
                size
        );

        return ResponseEntity.ok(
                supplierSubscriptionService.checkSubscriptions(
                        supplierEmail,
                        page,
                        size
                )
        );
    }
}