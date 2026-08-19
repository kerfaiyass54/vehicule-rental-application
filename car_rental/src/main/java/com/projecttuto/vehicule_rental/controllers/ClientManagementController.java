package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.ClientAdminDTO;
import com.projecttuto.vehicule_rental.services.ClientManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Client Management")
public class ClientManagementController {

    private final ClientManagementService clientManagementService;

    @Operation(summary = "Get all client emails")
    @GetMapping("/emails")
    public ResponseEntity<List<String>> getClientEmails() {

        log.info("Fetching all client emails");

        return ResponseEntity.ok(
                clientManagementService.getCLientEmails()
        );
    }

    @Operation(summary = "Get all clients")
    @GetMapping
    public ResponseEntity<Page<ClientAdminDTO>> getClients(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            int size) {

        log.info(
                "Fetching clients - page: {}, size: {}",
                page,
                size
        );

        return ResponseEntity.ok(
                clientManagementService.getClients(page, size)
        );
    }

    @Operation(summary = "Get client by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClientAdminDTO> getClient(
            @PathVariable
            @Positive(message = "Client id must be positive")
            Long id) {

        log.info("Fetching client with id: {}", id);

        return ResponseEntity.ok(
                clientManagementService.getClient(id)
        );
    }

    @Operation(summary = "Update client")
    @PutMapping("/{id}")
    public ResponseEntity<ClientAdminDTO> updateClient(
            @PathVariable
            @Positive(message = "Client id must be positive")
            Long id,

            @Valid
            @RequestBody
            ClientAdminDTO dto) {

        log.info("Updating client with id: {}", id);

        ClientAdminDTO updatedClient =
                clientManagementService.updateClient(id, dto);

        return ResponseEntity.ok(updatedClient);
    }

    @Operation(summary = "Delete client")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @PathVariable
            @Positive(message = "Client id must be positive")
            Long id) {

        log.info("Deleting client with id: {}", id);

        clientManagementService.deleteClient(id);

        return ResponseEntity.noContent().build();
    }
}