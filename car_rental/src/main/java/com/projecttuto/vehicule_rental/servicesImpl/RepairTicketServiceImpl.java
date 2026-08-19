package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;
import com.projecttuto.vehicule_rental.entities.Demand;
import com.projecttuto.vehicule_rental.entities.Repair;
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

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            throw new RuntimeException("Repair center not found");
        }

        Pageable pageable = PageRequest.of(page, size);

        return ticketRepository.findByRepair(repair, pageable)
                .map(ticket -> {

                    RepairTicketDTO dto = new RepairTicketDTO();

                    dto.setIdTicket(ticket.getIdTicket());

                    dto.setClientName(ticket.getClient().getClientName());

                    dto.setVehiculeName(ticket.getVehicle().getVehicleName());

                    dto.setTicketType(ticket.getType());

                    dto.setDescription(ticket.getDescription());

                    dto.setDateTicket(ticket.getDateInsert());

                    dto.setTicketStatus(ticket.getStatus());

                    Demand demand = demandRepository.findDemandByTicket(ticket);

                    if (demand != null) {

                        dto.setDemandType(demand.getType());

                        dto.setEstimatedTime(demand.getEstimatedTime());

                        dto.setSupplierName(
                                demand.getSupplier().getSupplierName());

                        dto.setDemandStatus(
                                demand.getStatusConfirm());
                    }

                    return dto;

                });

    }


}
