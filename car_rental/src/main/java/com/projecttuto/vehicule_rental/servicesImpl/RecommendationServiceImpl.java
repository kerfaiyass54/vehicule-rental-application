//package com.projecttuto.vehicule_rental.servicesImpl;
//
//import com.projecttuto.vehicule_rental.dto.CarRecommendationDTO;
//import com.projecttuto.vehicule_rental.dto.RecommendationResponseDTO;
//import com.projecttuto.vehicule_rental.entities.CarRecommendation;
//import com.projecttuto.vehicule_rental.entities.RecommendationDocument;
//import com.projecttuto.vehicule_rental.repositories.RecommendationRepository;
//import com.projecttuto.vehicule_rental.services.RecommendationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class RecommendationServiceImpl
//        implements RecommendationService {
//
//    private final RecommendationRepository repository;
//
//    @Override
//    public RecommendationResponseDTO getRecommendations(
//            Integer vehicleId
//    ) {
//
//        RecommendationDocument document =
//                repository
//                        .findByVehicleId(vehicleId)
//                        .orElseThrow(() ->
//                                new RuntimeException(
//                                        "Recommendations not found"
//                                ));
//
//        List<CarRecommendationDTO> recommendations =
//                document.getRecommendations()
//                        .stream()
//                        .map(this::mapToDTO)
//                        .toList();
//
//        return RecommendationResponseDTO
//                .builder()
//                .vehicleId(document.getVehicleId())
//                .recommendations(recommendations)
//                .build();
//
//    }
//
//    private CarRecommendationDTO mapToDTO(
//            CarRecommendation recommendation
//    ) {
//
//        return CarRecommendationDTO
//                .builder()
//                .carName(recommendation.getCarName())
//                .brand(recommendation.getBrand())
//                .price(recommendation.getPrice())
//                .horsepower(recommendation.getHorsepower())
//                .topSpeed(recommendation.getTopSpeed())
//                .acceleration0100(
//                        recommendation.getAcceleration0100()
//                )
//                .fuelType(recommendation.getFuelType())
//                .torque(recommendation.getTorque())
//                .build();
//
//    }
//
//}