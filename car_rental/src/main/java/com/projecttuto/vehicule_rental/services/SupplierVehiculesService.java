package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.VehiculeCreation;
import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeListDTO;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SupplierVehiculesService {
    public int countBySupplierEmailAndVehiculeStatus(String email, VehiculeStatus status);
    public List<VehiculeDTO> getVehiculesList(String email);
    public Vehicule addVehiculeNew(VehiculeCreation vehiculeCreation, String supplierEmail);
    public Page<VehiculeListDTO> getVehiculeList(int size, int page, String supplierName);

    public List<String> getVehiculesNames(String email);
    public List<Long> getVehiculesIds(String email);
    public Integer getTotalVehicules(String email);

    public Integer getSupplierVehicules(String email);


}
