package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;
import com.projecttuto.vehicule_rental.services.RepairTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repair-tickets")
@RequiredArgsConstructor
@Slf4j
public class RepairTicketController {

    private final RepairTicketService repairTicketService;

    @GetMapping
    public ResponseEntity<Page<RepairTicketDTO>> getTickets(
            @RequestParam String repairEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching tickets for repair center: {}, page: {}, size: {}",
                repairEmail,
                page,
                size
        );

        Page<RepairTicketDTO> tickets =
                repairTicketService.getTickets(
                        repairEmail,
                        page,
                        size
                );

        return ResponseEntity.ok(tickets);
    }
}