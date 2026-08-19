package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.SubscriptionResponseDTO;
import com.projecttuto.vehicule_rental.services.SupplierSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Slf4j
public class SupplierSubscriptionController {

    private final SupplierSubscriptionService supplierSubscriptionService;

    @GetMapping("/{supplierEmail}/subscriptions")
    public ResponseEntity<Page<SubscriptionResponseDTO>> getSubscriptions(
            @PathVariable String supplierEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching subscriptions for supplier: {}, page: {}, size: {}",
                supplierEmail,
                page,
                size
        );

        Page<SubscriptionResponseDTO> subscriptions =
                supplierSubscriptionService.checkSubscriptions(
                        supplierEmail,
                        page,
                        size
                );

        return ResponseEntity.ok(subscriptions);
    }
}