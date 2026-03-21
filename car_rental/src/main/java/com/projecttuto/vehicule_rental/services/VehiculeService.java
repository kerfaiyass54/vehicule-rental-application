package com.projecttuto.vehicule_rental.services;


import com.projecttuto.vehicule_rental.DTO.VehiculeDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeListDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeUpdate;
import org.springframework.data.domain.Page;

public interface VehiculeService {

    public VehiculeDTO addVehicule(VehiculeDTO vehiculeDTO);
    public Page<VehiculeListDTO> getVehiculeList(int size, int page, String supplierName);
    public VehiculeDTO getVehiculeById(Long id);
    public void updateVehicule(VehiculeUpdate vehiculeUpdate, Long id);




}
