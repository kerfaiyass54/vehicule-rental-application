package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.*;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
import com.projecttuto.vehicule_rental.exception.VehiculeRentalException;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.services.RepairOperationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepairOperationServiceImpl implements RepairOperationsService {

    private final RepairInfoRepository repairInfoRepository;
    private final TicketRepository ticketRepository;
    private final RepairRepository repairRepository;
    private final BuyingRepository buyingRepository;
    private final ClientRepository clientRepository;


    @Override
    public void cancelRepair(Long repairInfoId) {

        RepairInfo repairInfo = findRepairInfoById(repairInfoId);
        cancelRepairInfo(repairInfo);
        RepairInfoDTO repairInfoDTO = mapRepairInfoToDTO(repairInfo);
        Client client = clientRepository.findClientByClientName(repairInfoDTO.getClientName());
        client.setBudget(client.getBudget() + getTariff(repairInfoId, repairInfoDTO.getClientName()));
        clientRepository.save(client);
        repairInfoRepository.save(repairInfo);
    }


    @Override
    public RepairInfoDTO startRepair(Long repairInfoId) {

        RepairInfo repairInfo = findRepairInfoById(repairInfoId);
        repairInfo.setRepairStatus(RepairStatus.PENDING_FINISH);
        repairInfo.setDateStart(Instant.now());
        RepairInfo repairInfo1 = repairInfoRepository.save(repairInfo);
        RepairInfoDTO repairInfoDTO = mapRepairInfoToDTO(repairInfo1);
        Client client = clientRepository.findClientByClientName(repairInfoDTO.getClientName());
        client.setBudget(client.getBudget() - getTariff(repairInfoId, repairInfoDTO.getClientName()));
        clientRepository.save(client);
        return repairInfoDTO;
    }

    public Double getTariff(Long repairInfoId, String clientName) {
        RepairInfo repairInfo = findRepairInfoById(repairInfoId);
        Client client = clientRepository.findClientByClientName(clientName);
        Repair repair = repairInfo.getRepair();
        Vehicule vehicule = repairInfo.getVehicle();
        Ticket ticket = ticketRepository.findTicketByClientAndVehicleAndRepair(client, vehicule, repair);
        return ticket.getTariff();
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
                .findByRepairName(repairName)
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


        return dto;
    }


    private void setClientName(
            RepairInfo info,
            RepairInfoDTO dto) {

        Buying buying =
                buyingRepository.findBuyingByVehicle(
                        info.getVehicle()
                );

        if (buying != null) {

            dto.setClientName(
                    buying.getClient().getClientName()
            );
        }
    }

    public RepairInfoDTO getRepairInfo(Long repairInfoId){
        return mapRepairInfoToDTO(repairInfoRepository.findById(repairInfoId).orElse(null));
    }
}