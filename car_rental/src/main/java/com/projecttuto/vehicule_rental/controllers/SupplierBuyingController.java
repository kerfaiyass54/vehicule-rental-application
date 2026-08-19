package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.BuyingResponseDTO;
import com.projecttuto.vehicule_rental.services.SupplierBuyingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
@Tag(name = "Supplier Buying")
public class SupplierBuyingController {

    private final SupplierBuyingService supplierBuyingService;

    @Operation(
            summary = "Get supplier buyings",
            description = "Returns a paginated list of buyings associated with a supplier."
    )
    @GetMapping("/{supplierEmail}/buyings")
    public ResponseEntity<Page<BuyingResponseDTO>> getBuyings(
            @PathVariable
            @NotBlank(message = "Supplier email is required")
            @Email(message = "Invalid supplier email")
            String supplierEmail,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            @Max(value = 100, message = "Size must not exceed 100")
            int size) {

        log.info(
                "Fetching buyings for supplier: {}, page: {}, size: {}",
                supplierEmail,
                page,
                size
        );

        return ResponseEntity.ok(
                supplierBuyingService.checkBuyings(
                        supplierEmail,
                        page,
                        size
                )
        );
    }
}