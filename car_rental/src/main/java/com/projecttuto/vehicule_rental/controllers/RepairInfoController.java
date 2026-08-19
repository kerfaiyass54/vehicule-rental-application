package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.services.RepairInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repair-info")
@RequiredArgsConstructor
@Slf4j
public class RepairInfoController {

    private final RepairInfoService repairInfoService;

    @GetMapping("/{id}")
    public ResponseEntity<RepairInfoDTO> getRepairInfoById(
            @PathVariable Long id) {

        log.info("Fetching repair info with id: {}", id);

        return ResponseEntity.ok(
                repairInfoService.getRepairInfoById(id)
        );
    }
}