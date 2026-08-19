package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.BuyingResponseDTO;
import com.projecttuto.vehicule_rental.services.SupplierBuyingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierBuyingController {

    private final SupplierBuyingService supplierBuyingService;

    @GetMapping("/{supplierEmail}/buyings")
    public ResponseEntity<Page<BuyingResponseDTO>> getBuyings(
            @PathVariable String supplierEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching buyings for supplier: {}, page: {}, size: {}",
                supplierEmail,
                page,
                size
        );

        Page<BuyingResponseDTO> buyings =
                supplierBuyingService.checkBuyings(
                        supplierEmail,
                        page,
                        size
                );

        return ResponseEntity.ok(buyings);
    }
}