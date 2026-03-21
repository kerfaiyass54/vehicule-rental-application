package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.LocationDTO;
import com.projecttuto.vehicule_rental.DTO.RepairDTO;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.entities.Vehicule;

import java.util.List;

public interface RepairService {
    void addRepair(Repair repair, String location);
    void deleteRepair(long id);
    void updateRepair(RepairDTO repairDTO);
    RepairDTO getRepair(String nameRepair);
    void changeRepairPassword(Repair repair, String newPassword);
    List<Ticket> getTickets(String repairName);
    List<RepairInfo> getRepairInfo(String repairName);
    List<Vehicule> getVehicules(String repairName);
    void updateLocation(String repairName, String locationName);
    LocationDTO getLocation(String locationName);
}
