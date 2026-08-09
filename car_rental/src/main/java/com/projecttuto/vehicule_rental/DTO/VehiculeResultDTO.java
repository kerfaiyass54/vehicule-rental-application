package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor

public class VehiculeResultDTO {

    private Long idVehicule;

    private String nameVehicule;

    private String brand;

    private String color;

    private double price;

    private int highSpeed;

    private Transmission transmission;

    private VehiculeStatus vehiculeStatus;

    private String supplierName;

}
