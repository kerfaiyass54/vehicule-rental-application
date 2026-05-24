package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.DTO.RecommendationResponseDTO;
import com.projecttuto.vehicule_rental.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor

@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService service;

    @GetMapping("/{vehicleId}")
    public RecommendationResponseDTO getRecommendations(
            @PathVariable Integer vehicleId
    ) {

        return service.getRecommendations(vehicleId);

    }

}