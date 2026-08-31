package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.DemandResponseDTO;
import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.*;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.services.SupplierDemandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierDemandServiceImpl implements SupplierDemandService {

    private final DemandRepository demandRepository;
    private final TicketRepository ticketRepository;
    private final SupplierRepository supplierRepository;
    private final BuyingRepository  buyingRepository;
    private final RepairInfoRepository repairInfoRepository;

    @Override
    public DemandResponseDTO refuseDemand(Long demandId) {

        Demand demand = findDemandById(demandId);

        updateDemandStatus(demand, ConfirmStatus.REFUSED);
        updateTicketStatus(demand, RepairDemandStatus.REJECTED);

        Demand savedDemand = demandRepository.save(demand);

        return mapToDTO(savedDemand);
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
                buyingRepository.findBuyingByVehicle(
                        info.getVehicle()
                );

        if (buying != null) {

            dto.setClientName(
                    buying.getClient().getClientName()
            );
        }
    }

    @Override
    public RepairInfoDTO createRepairInfo(Long ticketId){
        RepairInfo repairInfo = new RepairInfo();
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        repairInfo.setVehicle(ticket.getVehicle());
        repairInfo.setRepair(ticket.getRepair());
        repairInfo.setRepairStatus(RepairStatus.PENDING_START);
        repairInfoRepository.save(repairInfo);
        return  mapRepairInfoToDTO(repairInfo);
    }

    @Override
    public Page<DemandResponseDTO> checkDemands(
            String supplierEmail,
            int page,
            int size) {

        Supplier supplier = supplierRepository.findSupplierByEmail(supplierEmail);

        log.info("Found suppliers: {}", supplier);

        Pageable pageable = PageRequest.of(page, size);

        return demandRepository
                .findBySupplier(supplier, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public DemandResponseDTO approveDemand(Long demandId) {

        Demand demand = findDemandById(demandId);

        updateDemandStatus(demand, ConfirmStatus.APPROVED);
        updateTicketStatus(demand, RepairDemandStatus.ACCEPTED);
        Ticket ticket = demand.getTicket();
        createRepairInfo(ticket.getIdTicket());
        Demand savedDemand = demandRepository.save(demand);

        return mapToDTO(savedDemand);
    }

    private Demand findDemandById(Long demandId) {

        return demandRepository.findById(demandId)
                .orElseThrow(() ->
                        new RuntimeException("Demand not found"));
    }

    private Supplier findSupplierByEmail(String supplierEmail) {

        Supplier supplier =
                supplierRepository.findSupplierByEmail(supplierEmail);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        return supplier;
    }

    private void updateDemandStatus(
            Demand demand,
            ConfirmStatus status) {

        demand.setStatusConfirm(status);
    }

    private void updateTicketStatus(
            Demand demand,
            RepairDemandStatus status) {

        Ticket ticket = demand.getTicket();

        ticket.setStatus(status);

        ticketRepository.save(ticket);
    }

    private DemandResponseDTO mapToDTO(Demand demand) {

        DemandResponseDTO dto = new DemandResponseDTO();

        dto.setIdDemand(demand.getIdDemand());
        dto.setType(demand.getType());
        dto.setDateAsk(demand.getDateAsk());
        dto.setEstimatedTime(demand.getEstimatedTime());
        dto.setStatus(demand.getStatusConfirm());

        dto.setVehiculeName(
                demand.getVehicle().getVehicleName());

        dto.setRepairName(
                demand.getTicket()
                        .getRepair()
                        .getRepairName());

        dto.setTicketId(
                demand.getTicket().getIdTicket());


        return dto;
    }


}