package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.ClientDashboardDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.enums.BuyStatus;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.repositories.BuyingRepository;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.SubscriptionRepository;
import com.projecttuto.vehicule_rental.repositories.TicketRepository;
import com.projecttuto.vehicule_rental.services.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final SubscriptionRepository subscriptionRepository;
    
    private final TicketRepository ticketRepository;

    private final BuyingRepository buyingRepository;








    @Override
    public ClientDashboardDTO getDashboard(String clientEmail) {

        Client client = clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        ClientDashboardDTO dto = new ClientDashboardDTO();

        dto.setClientName(client.getClientName());
        dto.setBudget(client.getBudget());

        dto.setTotalBuyings((Long) buyingRepository.countByClient(client));

        dto.setActiveBuyings((Long) buyingRepository.countByClientAndBuyStatus(
                client,
                BuyStatus.BEING_USED));

        dto.setTotalTickets((Long) ticketRepository.countByClient(client));

        dto.setPendingTickets((Long) ticketRepository.countByClientAndStatus(
                client,
                RepairDemandStatus.PENDING));

        dto.setCompletedTickets((Long) ticketRepository.countByClientAndStatus(
                client,
                RepairDemandStatus.COMPLETED));

        subscriptionRepository.findByClient(client).ifPresentOrElse(subscription -> {

            dto.setSubscribed(true);
            dto.setSubscriptionType(subscription.getSubscriptionType().name());

        }, () -> {

            dto.setSubscribed(false);
            dto.setSubscriptionType("NONE");

        });

        return dto;
    }








}
