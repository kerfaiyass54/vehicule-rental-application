package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.RepairDTO;
import com.projecttuto.vehicule_rental.DTO.RepairInfoDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeDTO;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.enums.RepairStatus;

import java.time.Instant;
import java.time.Instant;

public interface RepairInfoService {

    RepairInfoDTO getRepairInfoById(long id);
    RepairDTO getRepair(String name);

}
