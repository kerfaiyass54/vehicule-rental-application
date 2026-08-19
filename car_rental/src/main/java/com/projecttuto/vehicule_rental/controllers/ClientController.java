package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.ClientDashboardDTO;
import com.projecttuto.vehicule_rental.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{clientEmail}/dashboard")
    public ResponseEntity<ClientDashboardDTO> getDashboard(
            @PathVariable String clientEmail) {

        log.info("Fetching dashboard for client: {}", clientEmail);

        ClientDashboardDTO dashboard =
                clientService.getDashboard(clientEmail);

        return ResponseEntity.ok(dashboard);
    }
}