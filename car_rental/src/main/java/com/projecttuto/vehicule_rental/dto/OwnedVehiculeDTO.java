package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.Transmission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OwnedVehiculeDTO {

    @NotBlank
    private String nameVehicule;

    @NotBlank
    private String brand;

    @NotNull
    private Transmission transmission;

    @NotBlank
    private String supplierName;
}