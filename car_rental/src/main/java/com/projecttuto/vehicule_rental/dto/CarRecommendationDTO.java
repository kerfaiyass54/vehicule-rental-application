package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRecommendationDTO {

    @NotBlank
    private String carName;

    @NotBlank
    private String brand;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double price;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double horsepower;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double topSpeed;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double acceleration0100;

    @NotBlank
    private String fuelType;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double torque;
}