package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;
import com.projecttuto.vehicule_rental.services.RepairTicketService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @Operation(
            summary = "Get repair tickets",
            description = "Returns a paginated list of tickets assigned to a repair center."
    )
    @GetMapping
    public ResponseEntity<Page<RepairTicketDTO>> getTickets(
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

        Page<RepairTicketDTO> tickets =
                repairTicketService.getTickets(
                        repairEmail,
                        page,
                        size
                );

        return ResponseEntity.ok(tickets);
    }


}