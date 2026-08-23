package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.ClientDashboardDTO;
import com.projecttuto.vehicule_rental.dto.ClientDTO;
import com.projecttuto.vehicule_rental.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Client Management")
public class ClientController {

    private final ClientService clientService;


    // ---------------------------------------------------------
    // CLIENT DASHBOARD
    // ---------------------------------------------------------

    @Operation(summary = "Get client dashboard")
    @GetMapping("/{clientEmail}/dashboard")
    public ResponseEntity<ClientDashboardDTO> getDashboard(

            @PathVariable
            @Email(message = "Invalid client email")
            String clientEmail) {

        log.info(
                "Fetching dashboard for client: {}",
                clientEmail
        );

        ClientDashboardDTO dashboard =
                clientService.getDashboard(clientEmail);

        return ResponseEntity.ok(dashboard);
    }


    // ---------------------------------------------------------
    // CLIENT DETAILS
    // ---------------------------------------------------------

    @Operation(
            summary = "Get client by email",
            description = "Returns the client information associated with the given email."
    )
    @GetMapping("/{clientEmail}")
    public ResponseEntity<ClientDTO> getClient(

            @PathVariable
            @Email(message = "Invalid client email")
            String clientEmail) {

        log.info(
                "Fetching client information for: {}",
                clientEmail
        );

        ClientDTO client =
                clientService.getClient(clientEmail);

        return ResponseEntity.ok(client);
    }
}