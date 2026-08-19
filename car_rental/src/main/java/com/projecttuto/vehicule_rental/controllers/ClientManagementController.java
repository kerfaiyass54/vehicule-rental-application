package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.ClientAdminDTO;
import com.projecttuto.vehicule_rental.services.ClientManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientManagementController {

    private final ClientManagementService clientManagementService;

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getClientEmails() {

        log.info("Fetching all client emails");

        return ResponseEntity.ok(
                clientManagementService.getCLientEmails()
        );
    }

    @GetMapping
    public ResponseEntity<Page<ClientAdminDTO>> getClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching clients - page: {}, size: {}",
                page,
                size
        );

        return ResponseEntity.ok(
                clientManagementService.getClients(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientAdminDTO> getClient(
            @PathVariable Long id) {

        log.info("Fetching client with id: {}", id);

        return ResponseEntity.ok(
                clientManagementService.getClient(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientAdminDTO> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientAdminDTO dto) {

        log.info("Updating client with id: {}", id);

        ClientAdminDTO updatedClient =
                clientManagementService.updateClient(id, dto);

        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable Long id) {

        log.info("Deleting client with id: {}", id);

        clientManagementService.deleteClient(id);

        return ResponseEntity.noContent().build();
    }
}