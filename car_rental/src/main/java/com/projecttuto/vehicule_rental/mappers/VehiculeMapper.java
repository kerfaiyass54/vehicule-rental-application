package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.VehiculeResultDTO;
import com.projecttuto.vehicule_rental.entities.Vehicule;

public class VehiculeMapper {

    public static VehiculeResultDTO toDTO(Vehicule vehicule) {

        VehiculeResultDTO dto = new VehiculeResultDTO();

        dto.setIdVehicule(vehicule.getIdVehicule());
        dto.setNameVehicule(vehicule.getNameVehicule());
        dto.setBrand(vehicule.getBrand());
        dto.setColor(vehicule.getColor());
        dto.setPrice(vehicule.getPrice());
        dto.setHighSpeed(vehicule.getHighSpeed());
        dto.setTransmission(vehicule.getTransmission());
        dto.setVehiculeStatus(vehicule.getVehiculeStatus());

        dto.setSupplierName(
                vehicule.getSupplier() != null
                        ? vehicule.getSupplier().getSuppName()
                        : null);

        return dto;
    }
}