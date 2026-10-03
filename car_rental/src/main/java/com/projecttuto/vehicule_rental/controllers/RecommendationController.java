package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.events.RecommendationResponse;
import com.projecttuto.vehicule_rental.services.RecommendationGateway;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationGateway gateway;

    @PostMapping("/{email}/request")
    public ResponseEntity<Void> request(@PathVariable @Email String email) {
        gateway.request(email);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<RecommendationResponse> latest(@PathVariable @Email String email) {
        RecommendationResponse response = gateway.latest(email);
        return response == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(response);
    }
}
