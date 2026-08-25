package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.dto.VehiculeSupplierDTO;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.stereotype.Component;

@Component
public class VehiculeMapper {

    public VehiculeSupplierDTO toSupplierDTO(Vehicule vehicule) {

        if (vehicule == null) {
            return null;
        }

        VehiculeSupplierDTO dto = new VehiculeSupplierDTO();

        dto.setIdVehicule(vehicule.getIdVehicle());
        dto.setNameVehicule(vehicule.getVehicleName());
        dto.setColor(vehicule.getColor());
        dto.setBrand(vehicule.getBrand());
        dto.setPrice(vehicule.getPrice());
        dto.setHighSpeed(vehicule.getMaxSpeed());
        dto.setTransmission(vehicule.getTransmission());

        return dto;
    }
}