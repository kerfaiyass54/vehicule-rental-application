package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.DTO.RecommendationDTO;
import com.projecttuto.vehicule_rental.entities.Recommendation;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.repositories.RecommendationRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.RecommendationService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl
        implements RecommendationService {

    private final RecommendationRepository
            recommendationRepository;
    private final SupplierRepository supplierRepository;
    private final VehiculeRepository vehiculeRepository;

    public RecommendationServiceImpl(
            RecommendationRepository recommendationRepository, SupplierRepository supplierRepository, VehiculeRepository vehiculeRepository
    ) {
        this.recommendationRepository =
                recommendationRepository;
        this.supplierRepository = supplierRepository;
        this.vehiculeRepository = vehiculeRepository;
    }

    @Override
    public List<RecommendationDTO>
    getRecommendationsBySupplier(
            String supplierEmail
    ) {

        Supplier supplier =
                supplierRepository
                        .findSupplierByEmail(supplierEmail);

        List<Vehicule> vehicles =
                vehiculeRepository.findVehiculesBySupplier(
                        supplier
                );

        List<Long> vehicleIds =
                vehicles.stream()
                        .map(Vehicule::getIdVehicule)
                        .toList();

        List<Recommendation> recommendations =
                recommendationRepository
                        .findByVehicleIdIn(
                                vehicleIds
                        );

        return recommendations
                .stream()
                .map(rec ->
                        new RecommendationDTO(
                                rec.getId(),
                                rec.getVehicleId(),
                                rec.getRecommendationsJson()
                        )
                )
                .toList();
    }
}