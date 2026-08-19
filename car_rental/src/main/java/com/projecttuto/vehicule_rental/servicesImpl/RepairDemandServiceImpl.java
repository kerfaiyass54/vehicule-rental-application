package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.CreateDemandDTO;
import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;
import com.projecttuto.vehicule_rental.entities.Demand;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.repositories.DemandRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.repositories.TicketRepository;
import com.projecttuto.vehicule_rental.services.RepairDemandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepairDemandServiceImpl implements RepairDemandService {

    private final RepairRepository repairRepository;
    private final SupplierRepository supplierRepository;
    private final DemandRepository demandRepository;
    private final TicketRepository ticketRepository;


    @Override
    public RepairTicketDTO createDemand(CreateDemandDTO dto) {

        Repair repair = repairRepository.findRepairByEmail(dto.getRepairEmail());

        if (repair == null) {
            throw new RuntimeException("Repair center not found");
        }

        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getRepair().getIdRepair().equals(repair.getIdRepair())) {
            throw new RuntimeException("This ticket doesn't belong to this repair center.");
        }

        if (demandRepository.findDemandByTicket(ticket) != null) {
            throw new RuntimeException("A demand already exists for this ticket.");
        }

        Supplier supplier = supplierRepository.findSupplierBySuppName(dto.getSupplierName());

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        Demand demand = new Demand();

        demand.setType(dto.getType());

        demand.setEstimatedTime(dto.getEstimatedTime());

        demand.setStatusConfirm(ConfirmStatus.PENDING);

        demand.setTicket(ticket);

        demand.setSupplier(supplier);

        demand.setVehicle(ticket.getVehicle());

        Demand savedDemand = demandRepository.save(demand);

        RepairTicketDTO response = new RepairTicketDTO();

        response.setIdTicket(ticket.getIdTicket());

        response.setClientName(ticket.getClient().getClientName());

        response.setVehiculeName(ticket.getVehicle().getVehicleName());

        response.setTicketType(ticket.getType());

        response.setDescription(ticket.getDescription());

        response.setDateTicket(ticket.getDateInsert());

        response.setTicketStatus(ticket.getStatus());

        response.setDemandType(savedDemand.getType());

        response.setEstimatedTime(savedDemand.getEstimatedTime());

        response.setSupplierName(savedDemand.getSupplier().getSupplierName());

        response.setDemandStatus(savedDemand.getStatusConfirm());

        return response;
    }


}
