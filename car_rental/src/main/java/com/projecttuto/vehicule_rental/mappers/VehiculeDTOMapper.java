package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.VehiculeDTO;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.stereotype.Component;


@Component
public class VehiculeDTOMapper {

    public VehiculeDTO mapToDTO(Vehicule vehicule) {
        VehiculeDTO vehiculeDTO = new VehiculeDTO();
        vehiculeDTO.setVehiculeStatus(vehicule.getVehiculeStatus());
        vehiculeDTO.setNameVehicule(vehicule.getNameVehicule());
        vehiculeDTO.setTransmission(vehicule.getTransmission());
        vehiculeDTO.setBrand(vehicule.getBrand());
        vehiculeDTO.setColor(vehicule.getColor());
        vehiculeDTO.setPrice(vehicule.getPrice());
        vehiculeDTO.setIdVehicule(vehicule.getIdVehicule());
        vehiculeDTO.setHighSpeed(vehicule.getHighSpeed());
        return vehiculeDTO;
    }
}
