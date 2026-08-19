package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.DemandResponseDTO;
import com.projecttuto.vehicule_rental.services.SupplierDemandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class SupplierDemandController {

    private final SupplierDemandService supplierDemandService;

    @GetMapping("/suppliers/{supplierEmail}/demands")
    public ResponseEntity<Page<DemandResponseDTO>> getDemands(
            @PathVariable String supplierEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching demands for supplier: {}, page: {}, size: {}",
                supplierEmail,
                page,
                size
        );

        Page<DemandResponseDTO> demands =
                supplierDemandService.checkDemands(
                        supplierEmail,
                        page,
                        size
                );

        return ResponseEntity.ok(demands);
    }

    @PatchMapping("/supplier-demands/{demandId}/approve")
    public ResponseEntity<DemandResponseDTO> approveDemand(
            @PathVariable Long demandId) {

        log.info("Approving demand with id: {}", demandId);

        DemandResponseDTO response =
                supplierDemandService.approveDemand(demandId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/supplier-demands/{demandId}/refuse")
    public ResponseEntity<DemandResponseDTO> refuseDemand(
            @PathVariable Long demandId) {

        log.info("Refusing demand with id: {}", demandId);

        DemandResponseDTO response =
                supplierDemandService.refuseDemand(demandId);

        return ResponseEntity.ok(response);
    }
}