package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.CreateDemandDTO;
import com.projecttuto.vehicule_rental.dto.DemandDetailsDTO;
import com.projecttuto.vehicule_rental.dto.DemandsListPageDTO;
import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;
import com.projecttuto.vehicule_rental.services.RepairDemandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/v1/repair-demands")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Repair Demand Management")
public class RepairDemandController {

    private final RepairDemandService repairDemandService;

    // =========================================================
// GET DEMAND DETAILS
// =========================================================

    @Operation(
            summary = "Get demand details",
            description = "Returns detailed information about a repair demand."
    )
    @GetMapping("/{demandId}")
    public ResponseEntity<DemandDetailsDTO> getDemandDetails(
            @PathVariable
            @Min(value = 1, message = "Demand ID must be greater than 0")
            Long demandId) {

        log.info(
                "Fetching repair demand details: {}",
                demandId
        );

        DemandDetailsDTO demand =
                repairDemandService.getDemandDetails(demandId);

        return ResponseEntity.ok(demand);
    }


    // =========================================================
    // GET REPAIR DEMANDS
    // =========================================================

    @Operation(
            summary = "Get repair demands",
            description = "Returns a paginated list of demands belonging to a repair center."
    )
    @GetMapping("/repairs/{repairEmail}")
    public ResponseEntity<Page<DemandsListPageDTO>> getDemands(
            @PathVariable
            @NotBlank(message = "Repair email is required")
            @Email(message = "Repair email must be valid")
            String repairEmail,

            @RequestParam(defaultValue = "0")
            @Min(
                    value = 0,
                    message = "Page must be greater than or equal to 0"
            )
            int page,

            @RequestParam(defaultValue = "10")
            @Min(
                    value = 1,
                    message = "Size must be greater than 0"
            )
            int size) {

        log.info(
                "Fetching demands for repair: {}, page: {}, size: {}",
                repairEmail,
                page,
                size
        );

        Page<DemandsListPageDTO> demands =
                repairDemandService.getDemands(
                        size,
                        page,
                        repairEmail
                );

        return ResponseEntity.ok(demands);
    }


    // =========================================================
    // CREATE REPAIR DEMAND
    // =========================================================

    @Operation(
            summary = "Create a repair demand",
            description = "Creates a repair demand for a ticket."
    )
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