package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.services.RepairOperationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repair-operations")
@RequiredArgsConstructor
@Slf4j
public class RepairOperationsController {

    private final RepairOperationsService repairOperationsService;

    @PatchMapping("/{repairInfoId}/cancel")
    public ResponseEntity<Void> cancelRepair(
            @PathVariable Long repairInfoId) {

        log.info(
                "Cancelling repair with id: {}",
                repairInfoId
        );

        repairOperationsService.cancelRepair(repairInfoId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/start/{ticketId}")
    public ResponseEntity<RepairInfoDTO> startRepair(
            @PathVariable Long ticketId) {

        log.info(
                "Starting repair for ticket: {}",
                ticketId
        );

        RepairInfoDTO repairInfo =
                repairOperationsService.startRepair(ticketId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repairInfo);
    }

    @GetMapping
    public ResponseEntity<Page<RepairInfoDTO>> getRepairInfos(
            @RequestParam String repairEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching repair operations for repair center: {}, page: {}, size: {}",
                repairEmail,
                page,
                size
        );

        Page<RepairInfoDTO> repairInfos =
                repairOperationsService.getRepairInfos(
                        repairEmail,
                        page,
                        size
                );

        return ResponseEntity.ok(repairInfos);
    }

    @GetMapping("/by-name/{repairName}")
    public ResponseEntity<List<RepairInfo>> getRepairInfo(
            @PathVariable String repairName) {

        log.info(
                "Fetching repair information for repair: {}",
                repairName
        );

        return ResponseEntity.ok(
                repairOperationsService.getRepairInfo(repairName)
        );
    }
}