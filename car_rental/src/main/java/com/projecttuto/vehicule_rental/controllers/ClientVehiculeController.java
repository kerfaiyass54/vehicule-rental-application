package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.OwnedVehiculeDTO;
import com.projecttuto.vehicule_rental.services.ClientVehiculeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientVehiculeController {

    private final ClientVehiculeService clientVehiculeService;

    @GetMapping("/{clientEmail}/vehicles")
    public ResponseEntity<Page<OwnedVehiculeDTO>> getOwnedVehicules(
            @PathVariable String clientEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching owned vehicles for client: {}, page: {}, size: {}",
                clientEmail,
                page,
                size
        );

        Page<OwnedVehiculeDTO> vehicles =
                clientVehiculeService.getOwnedVehicules(
                        clientEmail,
                        page,
                        size
                );

        return ResponseEntity.ok(vehicles);
    }
}