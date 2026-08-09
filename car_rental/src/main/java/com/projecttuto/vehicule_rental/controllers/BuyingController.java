package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.entities.Buying;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projecttuto.vehicule_rental.services.BuyingService;

@RestController
@RequestMapping("/buying")
@CrossOrigin("*")
public class BuyingController {

    private final BuyingService buyingService;

    public BuyingController(BuyingService buyingService) {
        this.buyingService = buyingService;
    }


    @PostMapping("/")
    public ResponseEntity<Buying> addBuying(
            @RequestParam String vehiculeName,
            @RequestParam String clientName,
            @RequestParam int period) {

        Buying buying = buyingService.addBuying(vehiculeName, clientName, period);
        return ResponseEntity.status(HttpStatus.CREATED).body(buying);
    }

    @GetMapping("/client/{email}/buyings")
    public ResponseEntity<Page<Buying>> getBuyingByClient(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Buying> buyings = buyingService.getBuyingByClient(email, page, size);
        return ResponseEntity.ok(buyings);
    }
}
