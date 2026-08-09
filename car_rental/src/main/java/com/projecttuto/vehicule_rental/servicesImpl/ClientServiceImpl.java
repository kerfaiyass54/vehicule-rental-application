package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.DTO.*;
import com.projecttuto.vehicule_rental.entities.*;
import com.projecttuto.vehicule_rental.enums.BuyStatus;
import com.projecttuto.vehicule_rental.enums.StatusRepair;
import com.projecttuto.vehicule_rental.mappers.ClientDTOMapper;
import com.projecttuto.vehicule_rental.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.services.ClientService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ClientDTOMapper  clientDTOMapper;

    private final RepairRepository repairRepository;

    private final TicketRepository ticketRepository;

    private final BuyingRepository buyingRepository;

    @Override
    public void addClient(Client client, String locationName) {
        Optional<Location> location = locationRepository.findByName(locationName);
            client.setLocation(location.get());
            clientRepository.save(client);

    }

    @Override
    public Page<OwnedVehiculeDTO> getOwnedVehicules(String clientEmail, int page, int size) {

        Client client = clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Pageable pageable = PageRequest.of(page, size);

        return buyingRepository.findByClient(client, pageable)
                .map(buying -> {

                    Vehicule vehicule = buying.getVehicule();

                    OwnedVehiculeDTO dto = new OwnedVehiculeDTO();

                    dto.setNameVehicule(vehicule.getNameVehicule());
                    dto.setBrand(vehicule.getBrand());
                    dto.setTransmission(vehicule.getTransmission());

                    if (vehicule.getSupplier() != null) {
                        dto.setSupplierName(
                                vehicule.getSupplier().getSuppName());
                    }

                    return dto;
                });
    }

    @Override
    public ClientDTO getClient(long id){
        ClientDTO clientDTO = new ClientDTO();
        Client client = clientRepository.findById(id).get();
        clientDTO.setIdClient(client.getIdClient());
        clientDTO.setNameClient(client.getNameClient());
        clientDTO.setBudget(client.getBudget());
        if(client.getLocation() != null){
            clientDTO.setLocationName(client.getLocation().getName());
        }
        return clientDTO;
    }

    @Override
    public Client updateClient(String email, ClientUpdateDTO clientUpdateDTO) {

        Client client = clientRepository.findClientByEmail(email);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        if (clientUpdateDTO.getNameClient() != null && !clientUpdateDTO.getNameClient().isBlank()) {
            client.setNameClient(clientUpdateDTO.getNameClient());
        }

        if (clientUpdateDTO.getNationality() != null && !clientUpdateDTO.getNationality().isBlank()) {
            client.setNationality(clientUpdateDTO.getNationality());
        }

        if (clientUpdateDTO.getBudget() > 0) {
            client.setBudget(clientUpdateDTO.getBudget());
        }

        return clientRepository.save(client);
    }

    @Override
    public LocationDTO updateClientLocation(String clientEmail, LocationDTO locationDTO) {

        Client client = clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Location location = locationRepository.findById(locationDTO.getIdLoc())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        client.setLocation(location);
        clientRepository.save(client);

        return locationDTO;
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
        ticket.setDecription(dto.getDecription());
        ticket.setDateInsert(Instant.now());
        ticket.setStatus(StatusRepair.PENDING);
        ticket.setTarif(0);
        ticket.setClient(client);
        ticket.setVehicule(vehicule);
        ticket.setRepair(repair);

        Ticket saved = ticketRepository.save(ticket);

        TicketInfoDTO response = new TicketInfoDTO();

        response.setIdTicket(saved.getIdTicket());
        response.setType(saved.getType());
        response.setDescription(saved.getDecription());
        response.setDateInsert(saved.getDateInsert());
        response.setStatus(saved.getStatus());
        response.setTarif(saved.getTarif());
        response.setClientName(saved.getClient().getNameClient());
        response.setVehiculeName(saved.getVehicule().getNameVehicule());
        response.setRepairName(saved.getRepair().getNameRepair());

        return response;
    }

    @Override
    public ClientDashboardDTO getDashboard(String clientEmail) {

        Client client = clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        ClientDashboardDTO dto = new ClientDashboardDTO();

        dto.setClientName(client.getNameClient());
        dto.setBudget(client.getBudget());

        dto.setTotalBuyings((int) buyingRepository.countByClient(client));

        dto.setActiveBuyings((int) buyingRepository.countByClientAndBuyStatus(
                client,
                BuyStatus.BEING_USED));

        dto.setTotalTickets((int) ticketRepository.countByClient(client));

        dto.setPendingTickets((int) ticketRepository.countByClientAndStatus(
                client,
                StatusRepair.PENDING));

        dto.setCompletedTickets((int) ticketRepository.countByClientAndStatus(
                client,
                StatusRepair.COMPLETED));

        subscriptionRepository.findByClient(client).ifPresentOrElse(subscription -> {

            dto.setSubscribed(true);
            dto.setSubscriptionType(subscription.getType().name());

        }, () -> {

            dto.setSubscribed(false);
            dto.setSubscriptionType("NONE");

        });

        return dto;
    }

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
                    dto.setDescription(ticket.getDecription());
                    dto.setDateInsert(ticket.getDateInsert());
                    dto.setStatus(ticket.getStatus());
                    dto.setTarif(ticket.getTarif());

                    dto.setClientName(ticket.getClient().getNameClient());
                    dto.setVehiculeName(ticket.getVehicule().getNameVehicule());

                    if (ticket.getRepair() != null) {
                        dto.setRepairName(ticket.getRepair().getNameRepair());
                    }

                    return dto;
                });
    }

    @Override
    public void updateClient(ClientDTO clientDTO, long id){
        Client client = clientRepository.findById(id).get();
        client.setNameClient(clientDTO.getNameClient());
        client.setBudget(clientDTO.getBudget());
        clientRepository.save(client);

    }





    @Override
    public void deleteClient(long id){
        clientRepository.delete(clientRepository.findById(id).get());
    }



    @Override
    public List<String> getCLientEmails(){
        return clientRepository.findAll().stream().map(Client::getEmail).toList();
    }



}
