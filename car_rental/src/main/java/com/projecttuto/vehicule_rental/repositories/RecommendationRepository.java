package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.RecommendationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface RecommendationRepository
        extends ElasticsearchRepository<RecommendationDocument, String> {

    Optional<RecommendationDocument> findByVehicleId(Integer vehicleId);

}