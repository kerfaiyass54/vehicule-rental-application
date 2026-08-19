package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.RepairDashboardDTO;
import com.projecttuto.vehicule_rental.dto.RepairProfileDTO;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.services.RepairDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepairDetailsServiceImpl implements RepairDetailsService {

    private final RepairRepository  repairRepository;
    private final TicketRepository ticketRepository;
    private final DemandRepository demandRepository;
    private final RepairInfoRepository repairInfoRepository;
    private final LocationRepository locationRepository;

    @Override
    public RepairProfileDTO updateLocation(
            String repairEmail,
            Long locationId) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            throw new RuntimeException("Repair center not found");
        }

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() ->
                        new RuntimeException("Location not found"));

        repair.setLocation(location);

        repairRepository.save(repair);

        RepairProfileDTO dto = new RepairProfileDTO();

        dto.setIdRepair(repair.getIdRepair());

        dto.setNameRepair(repair.getRepairName());

        dto.setEmail(repair.getEmail());

        dto.setLocationName(location.getLocationName());

        dto.setCountry(location.getCountry());

        dto.setPosition(location.getPosition());

        return dto;
    }

    @Override
    public RepairProfileDTO getInfo(String repairEmail) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            throw new RuntimeException("Repair center not found");
        }

        RepairProfileDTO dto = new RepairProfileDTO();

        dto.setIdRepair(repair.getIdRepair());

        dto.setNameRepair(repair.getRepairName());

        dto.setEmail(repair.getEmail());

        if (repair.getLocation() != null) {

            dto.setLocationName(repair.getLocation().getLocationName());

            dto.setCountry(repair.getLocation().getCountry());

            dto.setPosition(repair.getLocation().getPosition());

        }

        return dto;
    }


    @Override
    public RepairDashboardDTO getDashboard(String repairEmail) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            throw new RuntimeException("Repair center not found");
        }

        RepairDashboardDTO dto = new RepairDashboardDTO();

        dto.setRepairName(repair.getRepairName());

        if (repair.getLocation() != null) {
            dto.setLocation(repair.getLocation().getLocationName());
        }

        // Tickets
        dto.setTotalTickets((Long) ticketRepository.countByRepair(repair));

        dto.setPendingTickets((Long) ticketRepository.countByRepairAndStatus(
                repair,
                RepairDemandStatus.PENDING));

        dto.setAcceptedTickets((Long) ticketRepository.countByRepairAndStatus(
                repair,
                RepairDemandStatus.ACCEPTED));

        dto.setCompletedTickets((Long) ticketRepository.countByRepairAndStatus(
                repair,
                RepairDemandStatus.COMPLETED));

        // Repairs
        dto.setActiveRepairs((Long) repairInfoRepository.countByRepairAndRepairStatus(
                repair,
                RepairStatus.PENDING_FINISH));

        dto.setCompletedRepairs((Long) repairInfoRepository.countByRepairAndRepairStatus(
                repair,
                RepairStatus.FINISHED));

        dto.setCancelledRepairs((Long) repairInfoRepository.countByRepairAndRepairStatus(
                repair,
                RepairStatus.CANCELLED));

        // Demands
        dto.setTotalDemands((Long) demandRepository.countByTicketRepair(repair));

        dto.setPendingDemands((Long) demandRepository.countByTicketRepairAndStatusConfirm(
                repair,
                ConfirmStatus.PENDING));

        dto.setAcceptedDemands((Long) demandRepository.countByTicketRepairAndStatusConfirm(
                repair,
                ConfirmStatus.APPROVED));

        dto.setRejectedDemands((Long) demandRepository.countByTicketRepairAndStatusConfirm(
                repair,
                ConfirmStatus.REFUSED));

        return dto;
    }


}
