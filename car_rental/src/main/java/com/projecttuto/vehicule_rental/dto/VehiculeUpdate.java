package com.projecttuto.vehicule_rental.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class VehiculeUpdate {

    private Long idVehicule;
    private String color;
    private double price;
    private int highSpeed;

}
