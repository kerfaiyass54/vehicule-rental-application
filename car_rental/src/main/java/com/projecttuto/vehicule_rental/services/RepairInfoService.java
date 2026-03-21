package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.RepairDTO;
import com.projecttuto.vehicule_rental.DTO.RepairInfoDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeDTO;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.enums.RepairStatus;

import java.time.Instant;
import java.time.Instant;

public interface RepairInfoService {
    void updateDateStart(Instant date, RepairInfo repairInfo);
    void updateStatus(RepairInfo repairInfo, RepairStatus repairStatus);
    RepairInfoDTO getRepairInfoById(long id);
    VehiculeDTO getVehiculeById(String name);
    RepairDTO getRepair(String name);
    void addReparation(String vehiculeName, String repairName, Instant date);

}
