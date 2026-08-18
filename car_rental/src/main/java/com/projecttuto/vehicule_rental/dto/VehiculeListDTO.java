package com.projecttuto.vehicule_rental.dto;



import com.projecttuto.vehicule_rental.enums.Transmission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class VehiculeListDTO {

    private Long idVehicule;
    private String nameVehicule;
    private double price;
    private Transmission transmission;

}
