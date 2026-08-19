package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.OpenTicketDTO;
import com.projecttuto.vehicule_rental.dto.TicketInfoDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.TicketRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.ClientTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@Slf4j
@RequiredArgsConstructor
public class ClientTicketServiceImpl implements ClientTicketService {

    private final ClientRepository clientRepository;

    private final TicketRepository ticketRepository;

    private final RepairRepository repairRepository;

    private final VehiculeRepository vehiculeRepository;


    @Override
    public Page<TicketInfoDTO> getClientTickets(String clientEmail, int page, int size) {

        Client client = clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Pageable pageable = PageRequest.of(page, size);

        return ticketRepository.findByClient(client, pageable)
                .map(ticket -> {

                    TicketInfoDTO dto = new TicketInfoDTO();

                    dto.setIdTicket(ticket.getIdTicket());
                    dto.setType(ticket.getType());
                    dto.setDescription(ticket.getDescription());
                    dto.setDateInsert(ticket.getDateInsert());
                    dto.setStatus(ticket.getStatus());
                    dto.setTarif(ticket.getTariff());

                    dto.setClientName(ticket.getClient().getClientName());
                    dto.setVehiculeName(ticket.getVehicle().getVehicleName());

                    if (ticket.getRepair() != null) {
                        dto.setRepairName(ticket.getRepair().getRepairName());
                    }

                    return dto;
                });
    }



    @Override
    public TicketInfoDTO openTicket(OpenTicketDTO dto) {

        Client client = clientRepository.findClientByEmail(dto.getClientEmail());

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Vehicule vehicule = vehiculeRepository.findVehiculeByNameVehicule(dto.getVehiculeName());

        if (vehicule == null) {
            throw new RuntimeException("Vehicule not found");
        }

        Repair repair = repairRepository.findRepairByNameRepair(dto.getRepairName());

        if (repair == null) {
            throw new RuntimeException("Repair not found");
        }

        Ticket ticket = new Ticket();

        ticket.setType(dto.getType());
        ticket.setDescription(dto.getDecription());
        ticket.setDateInsert(Instant.now());
        ticket.setStatus(RepairDemandStatus.PENDING);
        ticket.setTariff(0.0);
        ticket.setClient(client);
        ticket.setVehicle(vehicule);
        ticket.setRepair(repair);

        Ticket saved = ticketRepository.save(ticket);

        TicketInfoDTO response = new TicketInfoDTO();

        response.setIdTicket(saved.getIdTicket());
        response.setType(saved.getType());
        response.setDescription(saved.getDescription());
        response.setDateInsert(saved.getDateInsert());
        response.setStatus(saved.getStatus());
        response.setTarif(saved.getTariff());
        response.setClientName(saved.getClient().getClientName());
        response.setVehiculeName(saved.getVehicle().getVehicleName());
        response.setRepairName(saved.getRepair().getRepairName());

        return response;
    }


}
