package com.projecttuto.vehicule_rental.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationResponseDTO {

    private Integer vehicleId;

    private List<CarRecommendationDTO> recommendations;

}