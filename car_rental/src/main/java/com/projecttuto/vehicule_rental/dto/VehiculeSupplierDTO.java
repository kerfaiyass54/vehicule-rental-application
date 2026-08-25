package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.Transmission;
import lombok.Data;


@Data
public class VehiculeSupplierDTO {

    private Long idVehicule;
    private String nameVehicule;
    private String color;
    private String brand;
    private double price;
    private int highSpeed;
    private Transmission transmission;
}
