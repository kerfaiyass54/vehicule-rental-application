package com.projecttuto.vehicule_rental.services;


import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeListDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeResultDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeUpdate;
import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import org.springframework.data.domain.Page;

public interface VehiculeService {

    public VehiculeDTO addVehicule(VehiculeDTO vehiculeDTO);
    public Page<VehiculeListDTO> getVehiculeList(int size, int page, String supplierName);
    public VehiculeDTO getVehiculeById(Long id);
    public void updateVehicule(VehiculeUpdate vehiculeUpdate, Long id);
    Page<VehiculeResultDTO> searchVehicules(
            String keyword,
            Transmission transmission,
            VehiculeStatus status,
            Double minPrice,
            Double maxPrice,
            int page,
            int size);




}
