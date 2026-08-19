package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.services.RepairOperationsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repair-operations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RepairOperationsController {

    private final RepairOperationsService repairOperationsService;

    @Operation(
            summary = "Cancel a repair",
            description = "Cancels an existing repair operation."
    )
    @PatchMapping("/{repairInfoId}/cancel")
    public ResponseEntity<Void> cancelRepair(
            @PathVariable
            @Min(value = 1, message = "Repair info ID must be greater than 0")
            Long repairInfoId) {

        log.info(
                "Cancelling repair with id: {}",
                repairInfoId
        );

        repairOperationsService.cancelRepair(repairInfoId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Start a repair",
            description = "Starts a repair operation for an accepted ticket."
    )
    @PostMapping("/start/{ticketId}")
    public ResponseEntity<RepairInfoDTO> startRepair(
            @PathVariable
            @Min(value = 1, message = "Ticket ID must be greater than 0")
            Long ticketId) {

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

    @Operation(
            summary = "Get repair operations",
            description = "Returns a paginated list of repair operations for a repair center."
    )
    @GetMapping
    public ResponseEntity<Page<RepairInfoDTO>> getRepairInfos(
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

    @Operation(
            summary = "Get repair operations by repair center name",
            description = "Returns repair information associated with a repair center name."
    )
    @GetMapping("/by-name/{repairName}")
    public ResponseEntity<List<RepairInfo>> getRepairInfo(
            @PathVariable
            @NotBlank(message = "Repair name is required")
            String repairName) {

        log.info(
                "Fetching repair information for repair: {}",
                repairName
        );

        return ResponseEntity.ok(
                repairOperationsService.getRepairInfo(repairName)
        );
    }
}