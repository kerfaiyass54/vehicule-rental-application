package com.projecttuto.vehicule_rental.controllers;
import com.projecttuto.vehicule_rental.DTO.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.DTO.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.entities.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projecttuto.vehicule_rental.services.SubscriptionService;

@RestController
@RequestMapping("/subs")
@CrossOrigin("*")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/add")
    public void addSubscription(@RequestBody Subscription subscription,@RequestParam String emailClient,@RequestParam String nameSupplier){
        subscriptionService.addSubscription(subscription,emailClient,nameSupplier);
    }

    @PostMapping("/remove")
    public void removeSubscription(@RequestBody Subscription subscription){
        subscriptionService.removeSubscription(subscription);
    }

    @PostMapping("/renew/{nameClient}")
    public void renewSubscription(@RequestBody Subscription subscription,@PathVariable String nameClient,@RequestParam int time){
        subscriptionService.renewSubscription(subscription,nameClient,time);
    }

    @GetMapping("/subscript/{emailClient}")
    public boolean isClientSubscript(@PathVariable String emailClient,@RequestParam String nameSupplier){
        return subscriptionService.isClientSubscript(emailClient,nameSupplier);
    }

    @GetMapping("/get/{nameSupplier}")
    public SubscripionInfoDTO getSubscription(@RequestParam String emailClient,@PathVariable String nameSupplier){
        return subscriptionService.getSubscription(emailClient,nameSupplier);
    }

    @PostMapping("/add")
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
