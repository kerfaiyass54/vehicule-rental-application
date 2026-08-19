package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.OpenTicketDTO;
import com.projecttuto.vehicule_rental.dto.TicketInfoDTO;
import com.projecttuto.vehicule_rental.services.ClientTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Client Ticket Management")
public class ClientTicketController {

    private final ClientTicketService clientTicketService;

    @Operation(summary = "Get tickets for a client")
    @GetMapping("/clients/{clientEmail}")
    public ResponseEntity<Page<TicketInfoDTO>> getClientTickets(
            @PathVariable
            @Email(message = "Invalid client email")
            String clientEmail,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            int size) {

        log.info(
                "Fetching tickets for client: {}, page: {}, size: {}",
                clientEmail,
                page,
                size
        );

        Page<TicketInfoDTO> tickets =
                clientTicketService.getClientTickets(
                        clientEmail,
                        page,
                        size
                );

        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Open a repair ticket")
    @PostMapping
    public ResponseEntity<TicketInfoDTO> openTicket(
            @Valid @RequestBody OpenTicketDTO dto) {

        log.info(
                "Opening ticket for client: {} and vehicle: {}",
                dto.getClientEmail(),
                dto.getVehiculeName()
        );

        TicketInfoDTO ticket =
                clientTicketService.openTicket(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ticket);
    }
}