package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.services.BuyingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buyings")
@RequiredArgsConstructor
@Slf4j
public class BuyingController {

    private final BuyingService buyingService;

    @PostMapping
    public ResponseEntity<Buying> addBuying(
            @RequestParam String vehiculeName,
            @RequestParam String clientName,
            @RequestParam int period) {

        log.info(
                "Creating buying for vehicle '{}' and client '{}'",
                vehiculeName,
                clientName
        );

        Buying buying = buyingService.addBuying(
                vehiculeName,
                clientName,
                period
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(buying);
    }

    @GetMapping("/client/{clientEmail}")
    public ResponseEntity<Page<Buying>> getBuyingByClient(
            @PathVariable String clientEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching buyings for client email: {}, page: {}, size: {}",
                clientEmail,
                page,
                size
        );

        Page<Buying> buyings = buyingService.getBuyingByClient(
                clientEmail,
                page,
                size
        );

        return ResponseEntity.ok(buyings);
    }
}