package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
import com.projecttuto.vehicule_rental.repositories.*;
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

    private final SupplierRepository supplierRepository;

    private final RepairInfoRepository repairInfoRepository;

    private final TicketRepository ticketRepository;

    private final RepairRepository repairRepository;

    private final BuyingRepository buyingRepository;


    @Override
    public void cancelRepair(Long repairInfoId) {

        RepairInfo repairInfo = repairInfoRepository.findById(repairInfoId)
                .orElseThrow(() -> new RuntimeException("Repair not found"));

        repairInfo.setRepairStatus(RepairStatus.CANCELLED);

        repairInfoRepository.save(repairInfo);

    }

    @Override
    public RepairInfoDTO startRepair(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getStatus() != RepairDemandStatus.ACCEPTED) {
            throw new RuntimeException("Ticket must be accepted first.");
        }

        if (repairInfoRepository.findByVehicule(ticket.getVehicle()) != null) {
            throw new RuntimeException("Vehicle is already under repair.");
        }

        RepairInfo repairInfo = new RepairInfo();

        repairInfo.setVehicle(ticket.getVehicle());

        repairInfo.setRepair(ticket.getRepair());

        repairInfo.setRepairStatus(RepairStatus.PENDING_FINISH);

        RepairInfo saved = repairInfoRepository.save(repairInfo);

        ticket.setStatus(RepairDemandStatus.COMPLETED);

        ticketRepository.save(ticket);

        RepairInfoDTO dto = new RepairInfoDTO();

        dto.setIdRepairInfo(saved.getIdRepairInfo());

        dto.setVehiculeName(saved.getVehicle().getVehicleName());

        dto.setRepairName(saved.getRepair().getRepairName());

        dto.setClientName(ticket.getClient().getClientName());

        dto.setDateStart(saved.getDateStart());

        dto.setRepairStatus(saved.getRepairStatus());

        return dto;
    }


    @Override
    public Page<RepairInfoDTO> getRepairInfos(
            String repairEmail,
            int page,
            int size) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null)
            throw new RuntimeException("Repair center not found");

        Pageable pageable = PageRequest.of(page, size);

        return repairInfoRepository.findByRepair(repair, pageable)
                .map(info -> {

                    RepairInfoDTO dto = new RepairInfoDTO();

                    dto.setIdRepairInfo(info.getIdRepairInfo());

                    dto.setDateStart(info.getDateStart());

                    dto.setRepairStatus(info.getRepairStatus());

                    dto.setVehiculeName(
                            info.getVehicle().getVehicleName());

                    Buying buying = buyingRepository.findBuyingByVehicule(
                            info.getVehicle());

                    if (buying != null)
                        dto.setClientName(
                                buying.getClient().getClientName());

                    return dto;
                });
    }








    @Override
    public List<RepairInfo> getRepairInfo(String repairName){
        return repairRepository.findByNameRepair(repairName).get().getRepairInfos();
    }
}
