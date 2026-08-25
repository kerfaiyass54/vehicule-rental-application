package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.dto.SupplierInfoDTO;
import com.projecttuto.vehicule_rental.services.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "Create a client subscription",
            description = "Creates a new subscription for a client."
    )
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

    @Operation(
            summary = "Renew a client subscription",
            description = "Renews the subscription of the specified client."
    )
    @PutMapping("/{clientEmail}/subscription/renew")
    public ResponseEntity<SubscripionInfoDTO> renewSubscription(
            @PathVariable
            @NotBlank(message = "Client email is required")
            @Email(message = "Client email must be valid")
            String clientEmail) {

        log.info(
                "Renewing subscription for client: {}",
                clientEmail
        );

        SubscripionInfoDTO response =
                subscriptionService.renewSubscription(clientEmail);

        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
// SUBSCRIBED SUPPLIERS
// ---------------------------------------------------------

    @GetMapping("/subscribed")
    @Operation(
            summary = "Get subscribed suppliers",
            description = "Returns all suppliers to which the specified client is subscribed."
    )
    public ResponseEntity<List<SupplierInfoDTO>> getSubscribedSuppliers(
            @RequestParam Long clientId
    ) {

        return ResponseEntity.ok(
                subscriptionService.getSubscribedSuppliers(clientId)
        );
    }


// ---------------------------------------------------------
// UNSUBSCRIBED SUPPLIERS
// ---------------------------------------------------------

    @GetMapping("/unsubscribed")
    @Operation(
            summary = "Get unsubscribed suppliers",
            description = "Returns all suppliers to which the specified client is not subscribed."
    )
    public ResponseEntity<List<SupplierInfoDTO>> getUnsubscribedSuppliers(
            @RequestParam Long clientId
    ) {

        return ResponseEntity.ok(
                subscriptionService.getUnsubscribedSuppliers(clientId)
        );
    }

    @Operation(
            summary = "Cancel a client subscription",
            description = "Cancels the subscription of the specified client."
    )


    @DeleteMapping("/{clientEmail}/subscription")
    public ResponseEntity<Void> cancelSubscription(
            @PathVariable
            @NotBlank(message = "Client email is required")
            @Email(message = "Client email must be valid")
            String clientEmail) {

        log.info(
                "Cancelling subscription for client: {}",
                clientEmail
        );

        subscriptionService.cancelSubscription(clientEmail);

        return ResponseEntity.noContent().build();
    }
}