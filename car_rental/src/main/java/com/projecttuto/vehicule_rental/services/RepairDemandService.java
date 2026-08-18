package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.CreateDemandDTO;
import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;

public interface RepairDemandService {
    RepairTicketDTO createDemand(CreateDemandDTO dto);

}
