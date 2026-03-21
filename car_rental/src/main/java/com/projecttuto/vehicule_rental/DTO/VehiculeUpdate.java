package com.projecttuto.vehicule_rental.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VehiculeUpdate {

    private Long idVehicule;
    private String color;
    private double price;
    private int highSpeed;

}
