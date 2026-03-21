package com.projecttuto.vehicule_rental.DTO;



import com.projecttuto.vehicule_rental.enums.Transmission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class VehiculeListDTO {

    private Long idVehicule;
    private String nameVehicule;
    private double price;
    private Transmission transmission;
    private String category;

}
