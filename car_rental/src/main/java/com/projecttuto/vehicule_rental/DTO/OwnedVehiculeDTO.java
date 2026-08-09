package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.Transmission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OwnedVehiculeDTO {
    private String nameVehicule;

    private String brand;

    private Transmission transmission;

    private String supplierName;
}