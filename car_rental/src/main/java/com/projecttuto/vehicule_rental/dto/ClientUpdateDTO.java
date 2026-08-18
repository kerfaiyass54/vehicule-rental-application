package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientUpdateDTO {

    @NotBlank
    private String nameClient;

    @NotBlank
    private String nationality;

    @DecimalMin(value = "0.0")
    private double budget;
}