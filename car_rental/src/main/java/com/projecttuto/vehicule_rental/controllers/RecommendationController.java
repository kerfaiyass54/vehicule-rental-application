package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.DTO.RecommendationDTO;
import com.projecttuto.vehicule_rental.services.RecommendationService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin("*")
public class RecommendationController {

    private final RecommendationService
            recommendationService;

    public RecommendationController(
            RecommendationService recommendationService
    ) {
        this.recommendationService =
                recommendationService;
    }

    @GetMapping("/supplier/{supplierEmail}")
    public List<RecommendationDTO>
    getSupplierRecommendations(
            @PathVariable String supplierEmail
    ) {

        return recommendationService
                .getRecommendationsBySupplier(
                        supplierEmail
                );
    }
}