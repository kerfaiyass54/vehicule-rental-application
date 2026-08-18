package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.RecommendationResponseDTO;

public interface RecommendationService {

    RecommendationResponseDTO getRecommendations(Integer vehicleId);

}