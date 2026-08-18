package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.OwnedVehiculeDTO;
import org.springframework.data.domain.Page;

public interface ClientVehiculeService {
    Page<OwnedVehiculeDTO> getOwnedVehicules(String clientEmail, int page, int size);

}
