package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
import com.projecttuto.vehicule_rental.exception.VehiculeRentalException;
import com.projecttuto.vehicule_rental.repositories.BuyingRepository;
import com.projecttuto.vehicule_rental.repositories.RepairInfoRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.TicketRepository;
import com.projecttuto.vehicule_rental.services.RepairOperationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepairOperationServiceImpl implements RepairOperationsService {

    private final RepairInfoRepository repairInfoRepository;
    private final TicketRepository ticketRepository;
    private final RepairRepository repairRepository;
    private final BuyingRepository buyingRepository;


    @Override
    public void cancelRepair(Long repairInfoId) {

        RepairInfo repairInfo = findRepairInfoById(repairInfoId);

        cancelRepairInfo(repairInfo);

        repairInfoRepository.save(repairInfo);
    }


    @Override
    public RepairInfoDTO startRepair(Long ticketId) {

        Ticket ticket = findTicketById(ticketId);

        validateTicketStatus(ticket);

        validateVehicleNotUnderRepair(ticket);

        RepairInfo repairInfo = createRepairInfo(ticket);

        RepairInfo savedRepairInfo =
                repairInfoRepository.save(repairInfo);

        completeTicket(ticket);

        ticketRepository.save(ticket);

        return mapToDTO(savedRepairInfo, ticket);
    }


    @Override
    public Page<RepairInfoDTO> getRepairInfos(
            String repairEmail,
            int page,
            int size) {

        Repair repair = findRepairByEmail(repairEmail);

        Pageable pageable = PageRequest.of(page, size);

        return repairInfoRepository
                .findByRepair(repair, pageable)
                .map(this::mapRepairInfoToDTO);
    }


    @Override
    public List<RepairInfo> getRepairInfo(String repairName) {

        Repair repair = repairRepository
                .findByNameRepair(repairName)
                .orElseThrow(() ->
                        new VehiculeRentalException(
                                "Repair center not found"
                        )
                );

        return repair.getRepairInfos();
    }


    // =========================================================
    // FIND METHODS
    // =========================================================

    private RepairInfo findRepairInfoById(Long repairInfoId) {

        return repairInfoRepository.findById(repairInfoId)
                .orElseThrow(() ->
                        new VehiculeRentalException(
                                "Repair not found"
                        )
                );
    }


    private Ticket findTicketById(Long ticketId) {

        return ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new VehiculeRentalException(
                                "Ticket not found"
                        )
                );
    }


    private Repair findRepairByEmail(String repairEmail) {

        Repair repair =
                repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            throw new VehiculeRentalException(
                    "Repair center not found"
            );
        }

        return repair;
    }


    // =========================================================
    // VALIDATION METHODS
    // =========================================================

    private void validateTicketStatus(Ticket ticket) {

        if (ticket.getStatus() != RepairDemandStatus.ACCEPTED) {

            throw new VehiculeRentalException(
                    "Ticket must be accepted first."
            );
        }
    }


    private void validateVehicleNotUnderRepair(Ticket ticket) {

        if (repairInfoRepository.findByVehicule(
                ticket.getVehicle()) != null) {

            throw new VehiculeRentalException(
                    "Vehicle is already under repair."
            );
        }
    }


    // =========================================================
    // CREATION / UPDATE METHODS
    // =========================================================

    private RepairInfo createRepairInfo(Ticket ticket) {

        RepairInfo repairInfo = new RepairInfo();

        repairInfo.setVehicle(ticket.getVehicle());
        repairInfo.setRepair(ticket.getRepair());
        repairInfo.setRepairStatus(
                RepairStatus.PENDING_FINISH
        );

        return repairInfo;
    }


    private void cancelRepairInfo(RepairInfo repairInfo) {

        repairInfo.setRepairStatus(
                RepairStatus.CANCELLED
        );
    }


    private void completeTicket(Ticket ticket) {

        ticket.setStatus(
                RepairDemandStatus.COMPLETED
        );
    }


    // =========================================================
    // DTO MAPPING
    // =========================================================

    private RepairInfoDTO mapToDTO(
            RepairInfo repairInfo,
            Ticket ticket) {

        RepairInfoDTO dto = new RepairInfoDTO();

        dto.setIdRepairInfo(
                repairInfo.getIdRepairInfo()
        );

        dto.setVehiculeName(
                repairInfo.getVehicle().getVehicleName()
        );

        dto.setRepairName(
                repairInfo.getRepair().getRepairName()
        );

        dto.setClientName(
                ticket.getClient().getClientName()
        );

        dto.setDateStart(
                repairInfo.getDateStart()
        );

        dto.setRepairStatus(
                repairInfo.getRepairStatus()
        );

        return dto;
    }


    private RepairInfoDTO mapRepairInfoToDTO(
            RepairInfo info) {

        RepairInfoDTO dto = new RepairInfoDTO();

        dto.setIdRepairInfo(
                info.getIdRepairInfo()
        );

        dto.setDateStart(
                info.getDateStart()
        );

        dto.setRepairStatus(
                info.getRepairStatus()
        );

        dto.setVehiculeName(
                info.getVehicle().getVehicleName()
        );

        setClientName(info, dto);

        return dto;
    }


    private void setClientName(
            RepairInfo info,
            RepairInfoDTO dto) {

        Buying buying =
                buyingRepository.findBuyingByVehicule(
                        info.getVehicle()
                );

        if (buying != null) {

            dto.setClientName(
                    buying.getClient().getClientName()
            );
        }
    }
}