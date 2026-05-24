package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.DTO.RecommendationDTO;
import com.projecttuto.vehicule_rental.entities.Recommendation;
import com.projecttuto.vehicule_rental.repositories.RecommendationRepository;
import com.projecttuto.vehicule_rental.services.RecommendationService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl
        implements RecommendationService {

    private final RecommendationRepository
            recommendationRepository;

    public RecommendationServiceImpl(
            RecommendationRepository recommendationRepository
    ) {
        this.recommendationRepository =
                recommendationRepository;
    }

    @Override
    public List<RecommendationDTO>
    getRecommendationsBySupplier(
            Long supplierId
    ) {

        List<Recommendation> recommendations =
                recommendationRepository
                        .findBySupplierId(
                                supplierId
                        );

        return recommendations
                .stream()
                .map(rec ->
                        new RecommendationDTO(
                                rec.getVehicleId(),
                                rec.getRecommendationsJson()
                        )
                )
                .collect(Collectors.toList());
    }
}