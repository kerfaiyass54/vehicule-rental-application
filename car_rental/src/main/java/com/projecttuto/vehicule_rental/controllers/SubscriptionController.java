package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.dto.SupplierInfoDTO;
import com.projecttuto.vehicule_rental.enums.SubscriptionType;
import com.projecttuto.vehicule_rental.services.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    // =========================================================
    // CREATE SUBSCRIPTION
    // =========================================================

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

    // =========================================================
    // RENEW SUBSCRIPTION
    // =========================================================

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

    // =========================================================
    // GET SUBSCRIBED SUPPLIERS
    // =========================================================

    @GetMapping("/subscribed")
    @Operation(
            summary = "Get subscribed suppliers",
            description = "Returns all suppliers to which the client is subscribed."
    )
    public ResponseEntity<List<SupplierInfoDTO>> getSubscribedSuppliers(

            @RequestParam
            @NotBlank(message = "Client email is required")
            @Email(message = "Client email must be valid")
            String clientEmail) {

        log.info(
                "Fetching subscribed suppliers for client: {}",
                clientEmail
        );

        return ResponseEntity.ok(
                subscriptionService.getSubscribedSuppliers(clientEmail)
        );
    }

    // =========================================================
    // GET UNSUBSCRIBED SUPPLIERS
    // =========================================================

    @GetMapping("/unsubscribed")
    @Operation(
            summary = "Get unsubscribed suppliers",
            description = "Returns all suppliers to which the client is not subscribed."
    )
    public ResponseEntity<List<SupplierInfoDTO>> getUnsubscribedSuppliers(

            @RequestParam
            @NotBlank(message = "Client email is required")
            @Email(message = "Client email must be valid")
            String clientEmail) {

        log.info(
                "Fetching unsubscribed suppliers for client: {}",
                clientEmail
        );

        return ResponseEntity.ok(
                subscriptionService.getUnsubscribedSuppliers(clientEmail)
        );
    }

    // =========================================================
    // GET SUBSCRIPTION REDUCTION
    // =========================================================

    @GetMapping("/reduction")
    @Operation(
            summary = "Get subscription reduction",
            description = "Returns the reduction associated with the specified subscription type."
    )
    public ResponseEntity<Double> getReduction(

            @RequestParam
            SubscriptionType subscriptionType) {

        return ResponseEntity.ok(
                subscriptionService.getReduction(subscriptionType)
        );
    }

    // =========================================================
    // CANCEL SUBSCRIPTION
    // =========================================================

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

    // =========================================================
    // GET CLIENT SUBSCRIPTIONS
    // =========================================================

    @GetMapping("/{clientEmail}/subscription/list")
    @Operation(
            summary = "Get client subscriptions",
            description = "Returns the paginated subscriptions of the specified client."
    )
    public ResponseEntity<Page<SubscripionInfoDTO>> getSubscription(

            @Parameter(description = "Client email")
            @PathVariable
            @NotBlank(message = "Client email is required")
            @Email(message = "Invalid client email")
            String clientEmail,

            @Parameter(description = "Page number (zero-based)")
            @RequestParam(defaultValue = "0")
            @Min(
                    value = 0,
                    message = "Page must be greater than or equal to 0"
            )
            int page,

            @Parameter(description = "Number of subscriptions per page")
            @RequestParam(defaultValue = "10")
            @Min(
                    value = 1,
                    message = "Size must be greater than 0"
            )
            @Max(
                    value = 100,
                    message = "Size must not exceed 100"
            )
            int size) {

        log.info(
                "Fetching subscriptions for client: {}, page: {}, size: {}",
                clientEmail,
                page,
                size
        );

        return ResponseEntity.ok(
                subscriptionService.getSubscription(
                        clientEmail,
                        size,
                        page
                )
        );
    }
}