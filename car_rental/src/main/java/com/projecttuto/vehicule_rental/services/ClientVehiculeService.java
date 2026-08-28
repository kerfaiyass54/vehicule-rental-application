package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.OwnedVehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeSearchDTO;
import org.springframework.data.domain.Page;

public interface ClientVehiculeService {
    Page<OwnedVehiculeDTO> getOwnedVehicules(String clientEmail, int page, int size);

    Double getVehiculeTotalPrice(Long vehiculeId, Double reduction);

    Page<VehiculeSearchDTO> getSupplierVehicules(Long supplierId, int size, int page);



}
