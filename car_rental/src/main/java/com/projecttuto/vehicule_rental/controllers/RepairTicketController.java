package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.*;
import com.projecttuto.vehicule_rental.services.RepairTicketService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repair-tickets")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RepairTicketController {

    private final RepairTicketService repairTicketService;


    // =========================================================
    // GET REPAIR TICKETS
    // =========================================================

    @Operation(
            summary = "Get repair tickets",
            description = "Returns a paginated list of tickets assigned to a repair center."
    )
    @GetMapping
    public ResponseEntity<Page<TicketListDTO>> getTickets(
            @RequestParam
            @NotBlank(message = "Repair email is required")
            @Email(message = "Repair email must be valid")
            String repairEmail,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            int size) {

        log.info(
                "Fetching tickets for repair center: {}, page: {}, size: {}",
                repairEmail,
                page,
                size
        );

        Page<TicketListDTO> tickets =
                repairTicketService.getTickets(page, size, repairEmail);

        return ResponseEntity.ok(tickets);
    }


    // =========================================================
    // GET TICKET DETAILS
    // =========================================================

    @Operation(
            summary = "Get ticket information",
            description = "Returns the details of a specific repair ticket."
    )
    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDetailsDTO> getTicketInfo(
            @PathVariable
            @Positive(message = "Ticket ID must be greater than 0")
            Long ticketId) {

        log.info(
                "Fetching information for ticket: {}",
                ticketId
        );

        return ResponseEntity.ok(
                repairTicketService.getTicketInfo(ticketId)
        );
    }


    // =========================================================
    // GET TICKET CLIENT
    // =========================================================

    @Operation(
            summary = "Get ticket client",
            description = "Returns the client information associated with a repair ticket."
    )
    @GetMapping("/{ticketId}/client")
    public ResponseEntity<TicketClientDTO> getClient(
            @PathVariable
            @Positive(message = "Ticket ID must be greater than 0")
            Long ticketId) {

        log.info(
                "Fetching client information for ticket: {}",
                ticketId
        );

        return ResponseEntity.ok(
                repairTicketService.getClient(ticketId)
        );
    }


    // =========================================================
    // GET TICKET VEHICLE
    // =========================================================

    @Operation(
            summary = "Get ticket vehicle",
            description = "Returns the vehicle information associated with a repair ticket."
    )
    @GetMapping("/{ticketId}/vehicule")
    public ResponseEntity<TicketVehiculeDTO> getVehicule(
            @PathVariable
            @Positive(message = "Ticket ID must be greater than 0")
            Long ticketId) {

        log.info(
                "Fetching vehicle information for ticket: {}",
                ticketId
        );

        return ResponseEntity.ok(
                repairTicketService.getVehicule(ticketId)
        );
    }

}