package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.RepairDashboardDTO;
import com.projecttuto.vehicule_rental.dto.RepairProfileDTO;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
import com.projecttuto.vehicule_rental.exception.ResourceNotFoundException;
import com.projecttuto.vehicule_rental.repositories.DemandRepository;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import com.projecttuto.vehicule_rental.repositories.RepairInfoRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.TicketRepository;
import com.projecttuto.vehicule_rental.services.RepairDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepairDetailsServiceImpl implements RepairDetailsService {

    private final RepairRepository repairRepository;
    private final TicketRepository ticketRepository;
    private final DemandRepository demandRepository;
    private final RepairInfoRepository repairInfoRepository;
    private final LocationRepository locationRepository;

    @Override
    public RepairProfileDTO updateLocation(
            String repairEmail,
            Long locationId) {

        Repair repair = findRepairByEmail(repairEmail);
        Location location = findLocationById(locationId);

        updateRepairLocation(repair, location);

        return toProfileDTO(repair, location);
    }

    @Override
    public RepairProfileDTO getInfo(String repairEmail) {

        Repair repair = findRepairByEmail(repairEmail);

        return toProfileDTO(repair);
    }

    @Override
    public RepairDashboardDTO getDashboard(String repairEmail) {

        Repair repair = findRepairByEmail(repairEmail);

        RepairDashboardDTO dto = createDashboardDTO(repair);

        setLocationInformation(dto, repair);
        setTicketStatistics(dto, repair);
        setRepairStatistics(dto, repair);
        setDemandStatistics(dto, repair);

        return dto;
    }

    // ---------------------------------------------------------
    // Repair / Location
    // ---------------------------------------------------------

    private Repair findRepairByEmail(String repairEmail) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            log.warn(
                    "Repair center not found with email: {}",
                    repairEmail
            );

            throw new ResourceNotFoundException(
                    "Repair center not found with email: " + repairEmail
            );
        }

        return repair;
    }

    private Location findLocationById(Long locationId) {

        return locationRepository.findById(locationId)
                .orElseThrow(() -> {
                    log.warn(
                            "Location not found with id: {}",
                            locationId
                    );

                    return new ResourceNotFoundException(
                            "Location not found with id: " + locationId
                    );
                });
    }

    private void updateRepairLocation(
            Repair repair,
            Location location) {

        repair.setLocation(location);
        repairRepository.save(repair);
    }

    // ---------------------------------------------------------
    // Profile DTO
    // ---------------------------------------------------------

    private RepairProfileDTO toProfileDTO(Repair repair) {

        RepairProfileDTO dto = new RepairProfileDTO();

        dto.setIdRepair(repair.getIdRepair());
        dto.setNameRepair(repair.getRepairName());
        dto.setEmail(repair.getEmail());

        if (repair.getLocation() != null) {
            setLocationInformation(dto, repair.getLocation());
        }

        return dto;
    }

    private RepairProfileDTO toProfileDTO(
            Repair repair,
            Location location) {

        RepairProfileDTO dto = new RepairProfileDTO();

        dto.setIdRepair(repair.getIdRepair());
        dto.setNameRepair(repair.getRepairName());
        dto.setEmail(repair.getEmail());

        setLocationInformation(dto, location);

        return dto;
    }

    private void setLocationInformation(
            RepairProfileDTO dto,
            Location location) {

        dto.setLocationName(location.getLocationName());
        dto.setCountry(location.getCountry());
        dto.setPosition(location.getPosition());
    }

    // ---------------------------------------------------------
    // Dashboard
    // ---------------------------------------------------------

    private RepairDashboardDTO createDashboardDTO(Repair repair) {

        RepairDashboardDTO dto = new RepairDashboardDTO();

        dto.setRepairName(repair.getRepairName());

        return dto;
    }

    private void setLocationInformation(
            RepairDashboardDTO dto,
            Repair repair) {

        if (repair.getLocation() != null) {
            dto.setLocation(
                    repair.getLocation().getLocationName()
            );
        }
    }

    // ---------------------------------------------------------
    // Ticket statistics
    // ---------------------------------------------------------

    private void setTicketStatistics(
            RepairDashboardDTO dto,
            Repair repair) {

        dto.setTotalTickets(
                (Long) ticketRepository.countByRepair(repair)
        );

        dto.setPendingTickets(
                (Long) ticketRepository.countByRepairAndStatus(
                        repair,
                        RepairDemandStatus.PENDING
                )
        );

        dto.setAcceptedTickets(
                (Long) ticketRepository.countByRepairAndStatus(
                        repair,
                        RepairDemandStatus.ACCEPTED
                )
        );

        dto.setCompletedTickets(
                (Long) ticketRepository.countByRepairAndStatus(
                        repair,
                        RepairDemandStatus.COMPLETED
                )
        );
    }

    // ---------------------------------------------------------
    // Repair statistics
    // ---------------------------------------------------------

    private void setRepairStatistics(
            RepairDashboardDTO dto,
            Repair repair) {

        dto.setActiveRepairs(
                (Long) repairInfoRepository
                        .countByRepairAndRepairStatus(
                                repair,
                                RepairStatus.PENDING_FINISH
                        )
        );

        dto.setCompletedRepairs(
                (Long) repairInfoRepository
                        .countByRepairAndRepairStatus(
                                repair,
                                RepairStatus.FINISHED
                        )
        );

        dto.setCancelledRepairs(
                (Long) repairInfoRepository
                        .countByRepairAndRepairStatus(
                                repair,
                                RepairStatus.CANCELLED
                        )
        );
    }

    // ---------------------------------------------------------
    // Demand statistics
    // ---------------------------------------------------------

    private void setDemandStatistics(
            RepairDashboardDTO dto,
            Repair repair) {

        dto.setTotalDemands(
                (Long) demandRepository.countByTicketRepair(repair)
        );

        dto.setPendingDemands(
                (Long) demandRepository
                        .countByTicketRepairAndStatusConfirm(
                                repair,
                                ConfirmStatus.PENDING
                        )
        );

        dto.setAcceptedDemands(
                (Long) demandRepository
                        .countByTicketRepairAndStatusConfirm(
                                repair,
                                ConfirmStatus.APPROVED
                        )
        );

        dto.setRejectedDemands(
                (Long) demandRepository
                        .countByTicketRepairAndStatusConfirm(
                                repair,
                                ConfirmStatus.REFUSED
                        )
        );
    }
}