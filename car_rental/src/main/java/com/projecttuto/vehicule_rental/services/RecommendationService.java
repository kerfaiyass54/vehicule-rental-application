package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.RecommendationDTO;

import java.util.List;

public interface RecommendationService {

    List<RecommendationDTO>
    getRecommendationsBySupplier(
            String email
    );
}