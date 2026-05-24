package com.projecttuto.vehicule_rental.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRecommendationDTO {

    private String carName;

    private String brand;

    private Double price;

    private Double horsepower;

    private Double topSpeed;

    private Double acceleration0100;

    private String fuelType;

    private Double torque;

}