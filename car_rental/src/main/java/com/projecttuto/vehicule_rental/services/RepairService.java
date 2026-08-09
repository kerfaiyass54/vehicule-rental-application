package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.*;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RepairService {
    void deleteRepair(long id);
    void updateRepair(RepairDTO repairDTO);
    RepairDTO getRepair(String nameRepair);

    List<RepairInfo> getRepairInfo(String repairName);

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
