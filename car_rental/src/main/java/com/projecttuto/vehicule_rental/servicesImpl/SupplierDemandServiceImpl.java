package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.DemandResponseDTO;
import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Demand;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.repositories.BuyingRepository;
import com.projecttuto.vehicule_rental.repositories.DemandRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.repositories.TicketRepository;
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
    private final BuyingRepository buyingRepository;
    private final TicketRepository ticketRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public DemandResponseDTO refuseDemand(Long demandId) {

        Demand demand = findDemandById(demandId);

        updateDemandStatus(demand, ConfirmStatus.REFUSED);
        updateTicketStatus(demand, RepairDemandStatus.REJECTED);

        Demand savedDemand = demandRepository.save(demand);

        return mapToDTO(savedDemand);
    }

    @Override
    public Page<DemandResponseDTO> checkDemands(
            String supplierEmail,
            int page,
            int size) {

        Supplier supplier = findSupplierByEmail(supplierEmail);

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

        setClientName(dto, demand);

        return dto;
    }

    private void setClientName(
            DemandResponseDTO dto,
            Demand demand) {

        Buying buying =
                buyingRepository.findBuyingByVehicule(
                        demand.getVehicle());

        if (buying != null) {
            dto.setClientName(
                    buying.getClient().getClientName());
        }
    }
}