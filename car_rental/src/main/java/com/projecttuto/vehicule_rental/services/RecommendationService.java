package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.RecommendationResponseDTO;

public interface RecommendationService {

    RecommendationResponseDTO getRecommendations(Integer vehicleId);

}