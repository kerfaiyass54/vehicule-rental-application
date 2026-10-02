package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.ClientDTO;
import com.projecttuto.vehicule_rental.dto.ClientDashboardDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.enums.BuyStatus;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.services.ClientService;
import com.projecttuto.vehicule_rental.services.BudgetEventPublisher;
import com.projecttuto.vehicule_rental.services.LocationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TicketRepository ticketRepository;
    private final BuyingRepository buyingRepository;
    private final LocationRepository locationRepository;
    private final LocationEventPublisher locationEventPublisher;
    private final BudgetEventPublisher budgetEventPublisher;

    public Double getBudget(String clientEmail){
        return clientRepository.findClientByEmail(clientEmail).getBudget();
    }

    @Override
    public void publishBudgetSnapshot(String clientEmail) {
        Client client = findClientByEmail(clientEmail);
        budgetEventPublisher.publish("budget.snapshot", client.getIdClient(),
                client.getEmail(), client.getBudget(), client.getBudget());
    }

    public void reduceBudget(String clientEmail, Double valueToRemove){
        Client client = clientRepository.findClientByEmail(clientEmail);
        Double previousBudget = client.getBudget();
        client.setBudget(previousBudget - valueToRemove);
        clientRepository.save(client);
        budgetEventPublisher.publish("budget.updated", client.getIdClient(),
                client.getEmail(), previousBudget, client.getBudget());
        locationEventPublisher.publish("client.updated", "client", client.getIdClient(),
                client.getLocation(), client.getLocation());
    }

    @Override
    public ClientDashboardDTO getDashboard(String clientEmail) {

        Client client = findClientByEmail(clientEmail);

        ClientDashboardDTO dto = createDashboard(client);

        setBuyingStatistics(dto, client);
        setTicketStatistics(dto, client);

        return dto;
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
    // Dashboard
    // -------------------------------------------------------------------------

    private ClientDashboardDTO createDashboard(Client client) {

        return new ClientDashboardDTO();
    }

    private void setClientInformation(
            ClientDashboardDTO dto,
            Client client) {

        dto.setClientName(client.getClientName());
        dto.setBudget(client.getBudget());
    }

    // -------------------------------------------------------------------------
    // Buyings
    // -------------------------------------------------------------------------

    private void setBuyingStatistics(
            ClientDashboardDTO dto,
            Client client) {

        dto.setTotalBuyings(
                (Long) buyingRepository.countByClient(client)
        );

        dto.setActiveBuyings(
                (Long) buyingRepository.countByClientAndBuyStatus(
                        client,
                        BuyStatus.BEING_USED
                )
        );
    }

    // -------------------------------------------------------------------------
    // Tickets
    // -------------------------------------------------------------------------

    private void setTicketStatistics(
            ClientDashboardDTO dto,
            Client client) {

        dto.setTotalTickets(
                (Long) ticketRepository.countByClient(client)
        );

        dto.setPendingTickets(
                (Long) ticketRepository.countByClientAndStatus(
                        client,
                        RepairDemandStatus.PENDING
                )
        );

        dto.setCompletedTickets(
                (Long) ticketRepository.countByClientAndStatus(
                        client,
                        RepairDemandStatus.COMPLETED
                )
        );
    }

    // -------------------------------------------------------------------------
    // Subscription
    // -------------------------------------------------------------------------


    @Override
    public ClientDTO getClient(String clientEmail) {

        log.info(
                "Fetching client with email: {}",
                clientEmail
        );

        Client client = clientRepository
                .findByEmail(clientEmail);

        return mapToDTO(client);
    }

    // ---------------------------------------------------------
    // DTO MAPPER
    // ---------------------------------------------------------

    private ClientDTO mapToDTO(Client client) {

        ClientDTO dto = new ClientDTO();

        dto.setIdClient(
                client.getIdClient()
        );

        dto.setNameClient(
                client.getClientName()
        );

        dto.setNationality(
                client.getNationality()
        );

        dto.setBudget(
                client.getBudget() != null
                        ? client.getBudget()
                        : 0.0
        );

        dto.setEmail(
                client.getEmail()
        );

        dto.setRole(
                client.getRole()
        );

        return dto;
    }
}
