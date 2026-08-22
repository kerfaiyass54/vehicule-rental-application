package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.CreateDemandDTO;
import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;
import com.projecttuto.vehicule_rental.entities.Demand;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.exception.ResourceAlreadyExistsException;
import com.projecttuto.vehicule_rental.exception.ResourceNotFoundException;
import com.projecttuto.vehicule_rental.exception.UnauthorizedOperationException;
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

        Repair repair = findRepair(dto.getRepairEmail());

        Ticket ticket = findTicket(dto.getTicketId());

        validateTicketBelongsToRepair(ticket, repair);

        validateDemandDoesNotExist(ticket);

        Supplier supplier = findSupplier(dto.getSupplierName());

        Demand demand = createDemandEntity(dto, ticket, supplier);

        Demand savedDemand = demandRepository.save(demand);

        return buildRepairTicketResponse(ticket, savedDemand);
    }

    private Repair findRepair(String repairEmail) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            throw new ResourceNotFoundException(
                    "Repair center not found with email: " + repairEmail
            );
        }

        return repair;
    }

    private Ticket findTicket(Long ticketId) {

        return ticketRepository.findById(ticketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Ticket not found with id: " + ticketId
                        )
                );
    }

    private void validateTicketBelongsToRepair(
            Ticket ticket,
            Repair repair) {

        if (ticket.getRepair() == null ||
                !ticket.getRepair()
                        .getIdRepair()
                        .equals(repair.getIdRepair())) {

            throw new UnauthorizedOperationException(
                    "This ticket doesn't belong to this repair center."
            );
        }
    }

    private void validateDemandDoesNotExist(Ticket ticket) {

        if (demandRepository.findDemandByTicket(ticket) != null) {
            throw new ResourceAlreadyExistsException(
                    "A demand already exists for this ticket."
            );
        }
    }

    private Supplier findSupplier(String supplierName) {

        Supplier supplier =
                supplierRepository.findSupplierBySupplierName(supplierName);

        if (supplier == null) {
            throw new ResourceNotFoundException(
                    "Supplier not found with name: " + supplierName
            );
        }

        return supplier;
    }

    private Demand createDemandEntity(
            CreateDemandDTO dto,
            Ticket ticket,
            Supplier supplier) {

        Demand demand = new Demand();

        demand.setType(dto.getType());
        demand.setEstimatedTime(dto.getEstimatedTime());
        demand.setStatusConfirm(ConfirmStatus.PENDING);
        demand.setTicket(ticket);
        demand.setSupplier(supplier);
        demand.setVehicle(ticket.getVehicle());

        return demand;
    }

    private RepairTicketDTO buildRepairTicketResponse(
            Ticket ticket,
            Demand demand) {

        RepairTicketDTO response = new RepairTicketDTO();

        response.setIdTicket(ticket.getIdTicket());
        response.setClientName(ticket.getClient().getClientName());
        response.setVehiculeName(ticket.getVehicle().getVehicleName());
        response.setTicketType(ticket.getType());
        response.setDescription(ticket.getDescription());
        response.setDateTicket(ticket.getDateInsert());
        response.setTicketStatus(ticket.getStatus());

        response.setDemandType(demand.getType());
        response.setEstimatedTime(demand.getEstimatedTime());
        response.setSupplierName(
                demand.getSupplier().getSupplierName()
        );
        response.setDemandStatus(demand.getStatusConfirm());

        return response;
    }
}