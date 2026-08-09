package com.projecttuto.vehicule_rental.DTO;


import com.projecttuto.vehicule_rental.enums.Transmission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehiculeCreation {

    private String nameVehicule;
    private String color;
    private String brand;
    private double price;
    private int highSpeed;
    private Transmission transmission;
}
