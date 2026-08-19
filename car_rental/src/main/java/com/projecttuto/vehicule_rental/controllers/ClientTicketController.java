package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.OpenTicketDTO;
import com.projecttuto.vehicule_rental.dto.TicketInfoDTO;
import com.projecttuto.vehicule_rental.services.ClientTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Slf4j
public class ClientTicketController {

    private final ClientTicketService clientTicketService;

    @GetMapping("/client/{clientEmail}")
    public ResponseEntity<Page<TicketInfoDTO>> getClientTickets(
            @PathVariable String clientEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

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