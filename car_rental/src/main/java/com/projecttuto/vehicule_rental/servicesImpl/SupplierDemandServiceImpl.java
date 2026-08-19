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

        Demand demand = demandRepository.findById(demandId)
                .orElseThrow(() ->
                        new RuntimeException("Demand not found"));

        demand.setStatusConfirm(ConfirmStatus.REFUSED);

        Ticket ticket = demand.getTicket();

        ticket.setStatus(RepairDemandStatus.REJECTED);

        ticketRepository.save(ticket);

        Demand saved = demandRepository.save(demand);

        DemandResponseDTO dto = new DemandResponseDTO();

        dto.setIdDemand(saved.getIdDemand());

        dto.setType(saved.getType());

        dto.setDateAsk(saved.getDateAsk());

        dto.setEstimatedTime(saved.getEstimatedTime());

        dto.setStatus(saved.getStatusConfirm());

        dto.setVehiculeName(saved.getVehicle().getVehicleName());

        dto.setRepairName(saved.getTicket().getRepair().getRepairName());

        Buying buying =
                buyingRepository.findBuyingByVehicule(saved.getVehicle());

        if (buying != null)
            dto.setClientName(buying.getClient().getClientName());

        dto.setTicketId(saved.getTicket().getIdTicket());

        return dto;

    }

    @Override
    public Page<DemandResponseDTO> checkDemands(
            String supplierEmail,
            int page,
            int size) {

        Supplier supplier = supplierRepository.findSupplierByEmail(supplierEmail);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        Pageable pageable = PageRequest.of(page, size);

        return demandRepository.findBySupplier(supplier, pageable)
                .map(demand -> {

                    DemandResponseDTO dto = new DemandResponseDTO();

                    dto.setIdDemand(demand.getIdDemand());

                    dto.setType(demand.getType());

                    dto.setDateAsk(demand.getDateAsk());

                    dto.setEstimatedTime(demand.getEstimatedTime());

                    dto.setStatus(demand.getStatusConfirm());

                    dto.setVehiculeName(
                            demand.getVehicle().getVehicleName());

                    dto.setRepairName(
                            demand.getTicket().getRepair().getRepairName());

                    Buying buying =
                            buyingRepository.findBuyingByVehicule(
                                    demand.getVehicle());

                    if (buying != null) {
                        dto.setClientName(
                                buying.getClient().getClientName());
                    }

                    dto.setTicketId(
                            demand.getTicket().getIdTicket());

                    return dto;

                });

    }


    @Override
    public DemandResponseDTO approveDemand(Long demandId) {

        Demand demand = demandRepository.findById(demandId)
                .orElseThrow(() ->
                        new RuntimeException("Demand not found"));

        demand.setStatusConfirm(ConfirmStatus.APPROVED);

        Ticket ticket = demand.getTicket();

        ticket.setStatus(RepairDemandStatus.ACCEPTED);

        ticketRepository.save(ticket);

        Demand saved = demandRepository.save(demand);

        DemandResponseDTO dto = new DemandResponseDTO();

        dto.setIdDemand(saved.getIdDemand());

        dto.setType(saved.getType());

        dto.setDateAsk(saved.getDateAsk());

        dto.setEstimatedTime(saved.getEstimatedTime());

        dto.setStatus(saved.getStatusConfirm());

        dto.setVehiculeName(saved.getVehicle().getVehicleName());

        dto.setRepairName(saved.getTicket().getRepair().getRepairName());

        Buying buying =
                buyingRepository.findBuyingByVehicule(saved.getVehicle());

        if (buying != null)
            dto.setClientName(buying.getClient().getClientName());

        dto.setTicketId(saved.getTicket().getIdTicket());

        return dto;

    }

}
