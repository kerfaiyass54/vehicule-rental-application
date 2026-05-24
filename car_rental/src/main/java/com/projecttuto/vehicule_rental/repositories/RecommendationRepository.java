package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository
        extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByVehicleIdIn(
            List<Long> vehicleIds
    );
}