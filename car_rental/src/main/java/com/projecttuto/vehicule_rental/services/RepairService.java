package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.*;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.data.domain.Page;

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
    RepairDashboardDTO getDashboard(String repairEmail);

    Page<RepairInfoDTO> getRepairInfos(
            String repairEmail,
            int page,
            int size);
    Page<RepairTicketDTO> getTickets(
            String repairEmail,
            int page,
            int size);

    RepairTicketDTO createDemand(CreateDemandDTO dto);
    RepairInfoDTO startRepair(Long ticketId);

    Page<RepairInfoDTO> checkRepairs(String repairEmail,
                                     int page,
                                     int size);

    void cancelRepair(Long repairInfoId);

    RepairProfileDTO getInfo(String repairEmail);
    RepairProfileDTO updateLocation(
            String repairEmail,
            Long locationId);
}
