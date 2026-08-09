package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.SubscripionInfoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.projecttuto.vehicule_rental.services.SubscriptionService;

@RestController
@RequestMapping("/subscription")
@CrossOrigin("*")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }


    @PostMapping("/")
    public ResponseEntity<SubscripionInfoDTO> addSubscription(
            @RequestBody SubscripionInfoDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.addSubscription(dto));
    }

    @PutMapping("/renew/{email}")
    public ResponseEntity<SubscripionInfoDTO> renewSubscription(
            @PathVariable String email) {

        return ResponseEntity.ok(
                subscriptionService.renewSubscription(email));
    }

    @DeleteMapping("/cancel/{email}")
    public ResponseEntity<Void> cancelSubscription(
            @PathVariable String email) {

        subscriptionService.cancelSubscription(email);

        return ResponseEntity.noContent().build();
    }


}
