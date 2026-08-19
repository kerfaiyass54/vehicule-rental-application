package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.services.RepairInfoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repair-info")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RepairInfoController {

    private final RepairInfoService repairInfoService;

    @Operation(
            summary = "Get repair information",
            description = "Returns repair information for the specified repair info ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<RepairInfoDTO> getRepairInfoById(
            @PathVariable
            @Min(value = 1, message = "Repair info ID must be greater than 0")
            Long id) {

        log.info("Fetching repair info with id: {}", id);

        return ResponseEntity.ok(
                repairInfoService.getRepairInfoById(id)
        );
    }
}