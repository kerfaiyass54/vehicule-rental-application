package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.services.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscriptions")
    public ResponseEntity<SubscripionInfoDTO> addSubscription(
            @Valid @RequestBody SubscripionInfoDTO dto) {

        log.info(
                "Creating subscription for client: {}",
                dto.getClientEmail()
        );

        SubscripionInfoDTO response =
                subscriptionService.addSubscription(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{clientEmail}/subscription/renew")
    public ResponseEntity<SubscripionInfoDTO> renewSubscription(
            @PathVariable String clientEmail) {

        log.info(
                "Renewing subscription for client: {}",
                clientEmail
        );

        SubscripionInfoDTO response =
                subscriptionService.renewSubscription(clientEmail);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{clientEmail}/subscription")
    public ResponseEntity<Void> cancelSubscription(
            @PathVariable String clientEmail) {

        log.info(
                "Cancelling subscription for client: {}",
                clientEmail
        );

        subscriptionService.cancelSubscription(clientEmail);

        return ResponseEntity.noContent().build();
    }
}