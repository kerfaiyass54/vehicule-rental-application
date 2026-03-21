package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VehiculeDTO {
    private Long idVehicule;
    private String nameVehicule;
    private String color;
    private String brand;
    private double price;
    private int highSpeed;
    private Transmission transmission;
    private VehiculeStatus vehiculeStatus;
    private String category;
    private String supplier;

}
