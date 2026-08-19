package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;
import com.projecttuto.vehicule_rental.entities.Demand;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.exception.VehiculeRentalException;
import com.projecttuto.vehicule_rental.repositories.DemandRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.TicketRepository;
import com.projecttuto.vehicule_rental.services.RepairTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepairTicketServiceImpl implements RepairTicketService {

    private final RepairRepository repairRepository;
    private final TicketRepository ticketRepository;
    private final DemandRepository demandRepository;


    @Override
    public Page<RepairTicketDTO> getTickets(
            String repairEmail,
            int page,
            int size) {

        Repair repair = findRepairByEmail(repairEmail);

        Pageable pageable = createPageable(page, size);

        return ticketRepository
                .findByRepair(repair, pageable)
                .map(this::mapTicketToDTO);
    }


    // =========================================================
    // FIND METHODS
    // =========================================================

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
    // PAGINATION
    // =========================================================

    private Pageable createPageable(int page, int size) {

        return PageRequest.of(page, size);
    }


    // =========================================================
    // DTO MAPPING
    // =========================================================

    private RepairTicketDTO mapTicketToDTO(Ticket ticket) {

        RepairTicketDTO dto = new RepairTicketDTO();

        setTicketInformation(ticket, dto);
        setDemandInformation(ticket, dto);

        return dto;
    }


    private void setTicketInformation(
            Ticket ticket,
            RepairTicketDTO dto) {

        dto.setIdTicket(
                ticket.getIdTicket()
        );

        dto.setClientName(
                ticket.getClient().getClientName()
        );

        dto.setVehiculeName(
                ticket.getVehicle().getVehicleName()
        );

        dto.setTicketType(
                ticket.getType()
        );

        dto.setDescription(
                ticket.getDescription()
        );

        dto.setDateTicket(
                ticket.getDateInsert()
        );

        dto.setTicketStatus(
                ticket.getStatus()
        );
    }


    private void setDemandInformation(
            Ticket ticket,
            RepairTicketDTO dto) {

        Demand demand =
                demandRepository.findDemandByTicket(ticket);

        if (demand == null) {
            return;
        }

        dto.setDemandType(
                demand.getType()
        );

        dto.setEstimatedTime(
                demand.getEstimatedTime()
        );

        dto.setSupplierName(
                demand.getSupplier().getSupplierName()
        );

        dto.setDemandStatus(
                demand.getStatusConfirm()
        );
    }
}