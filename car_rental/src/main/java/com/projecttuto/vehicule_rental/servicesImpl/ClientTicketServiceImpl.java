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
    public Page<TicketInfoDTO> getClientTickets(
            String clientEmail,
            int page,
            int size) {

        Client client = findClientByEmail(clientEmail);

        Pageable pageable = createPageable(page, size);

        return ticketRepository
                .findByClient(client, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public TicketInfoDTO openTicket(OpenTicketDTO dto) {

        Client client = findClientByEmail(dto.getClientEmail());

        Vehicule vehicule = findVehiculeByName(dto.getVehiculeName());

        Repair repair = findRepairByName(dto.getRepairName());

        Ticket ticket = createTicket(dto, client, vehicule, repair);

        Ticket savedTicket = saveTicket(ticket);

        return mapToDTO(savedTicket);
    }

    // -------------------------------------------------------------------------
    // Client
    // -------------------------------------------------------------------------

    private Client findClientByEmail(String clientEmail) {

        Client client = clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        return client;
    }

    // -------------------------------------------------------------------------
    // Vehicle
    // -------------------------------------------------------------------------

    private Vehicule findVehiculeByName(String vehiculeName) {

        Vehicule vehicule =
                vehiculeRepository.findVehiculeByNameVehicule(vehiculeName);

        if (vehicule == null) {
            throw new RuntimeException("Vehicule not found");
        }

        return vehicule;
    }

    // -------------------------------------------------------------------------
    // Repair
    // -------------------------------------------------------------------------

    private Repair findRepairByName(String repairName) {

        Repair repair =
                repairRepository.findRepairByNameRepair(repairName);

        if (repair == null) {
            throw new RuntimeException("Repair not found");
        }

        return repair;
    }

    // -------------------------------------------------------------------------
    // Ticket
    // -------------------------------------------------------------------------

    private Ticket createTicket(
            OpenTicketDTO dto,
            Client client,
            Vehicule vehicule,
            Repair repair) {

        Ticket ticket = new Ticket();

        ticket.setType(dto.getType());
        ticket.setDescription(dto.getDecription());
        ticket.setDateInsert(Instant.now());
        ticket.setStatus(RepairDemandStatus.PENDING);
        ticket.setTariff(0.0);
        ticket.setClient(client);
        ticket.setVehicle(vehicule);
        ticket.setRepair(repair);

        return ticket;
    }

    private Ticket saveTicket(Ticket ticket) {

        return ticketRepository.save(ticket);
    }

    // -------------------------------------------------------------------------
    // Pagination
    // -------------------------------------------------------------------------

    private Pageable createPageable(int page, int size) {

        return PageRequest.of(page, size);
    }

    // -------------------------------------------------------------------------
    // Mapping
    // -------------------------------------------------------------------------

    private TicketInfoDTO mapToDTO(Ticket ticket) {

        TicketInfoDTO dto = new TicketInfoDTO();

        dto.setIdTicket(ticket.getIdTicket());
        dto.setType(ticket.getType());
        dto.setDescription(ticket.getDescription());
        dto.setDateInsert(ticket.getDateInsert());
        dto.setStatus(ticket.getStatus());
        dto.setTarif(ticket.getTariff());

        dto.setClientName(
                ticket.getClient().getClientName()
        );

        dto.setVehiculeName(
                ticket.getVehicle().getVehicleName()
        );

        setRepairName(dto, ticket);

        return dto;
    }

    private void setRepairName(
            TicketInfoDTO dto,
            Ticket ticket) {

        if (ticket.getRepair() != null) {
            dto.setRepairName(
                    ticket.getRepair().getRepairName()
            );
        }
    }
}