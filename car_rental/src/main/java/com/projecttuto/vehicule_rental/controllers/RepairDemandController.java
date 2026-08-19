package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.CreateDemandDTO;
import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;
import com.projecttuto.vehicule_rental.services.RepairDemandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repair-demands")
@RequiredArgsConstructor
@Slf4j
public class RepairDemandController {

    private final RepairDemandService repairDemandService;

    @PostMapping
    public ResponseEntity<RepairTicketDTO> createDemand(
            @Valid @RequestBody CreateDemandDTO dto) {

        log.info(
                "Creating repair demand for ticket: {}",
                dto.getTicketId()
        );

        RepairTicketDTO createdDemand =
                repairDemandService.createDemand(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdDemand);
    }
}