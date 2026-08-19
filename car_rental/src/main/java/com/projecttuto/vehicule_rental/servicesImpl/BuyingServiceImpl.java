package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.BuyStatus;
import com.projecttuto.vehicule_rental.repositories.BuyingRepository;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.BuyingService;
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
public class BuyingServiceImpl implements BuyingService {

    private final BuyingRepository buyingRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ClientRepository clientRepository;

    @Override
    public Buying addBuying(
            String vehiculeName,
            String clientName,
            int period) {

        Vehicule vehicule = findVehiculeByName(vehiculeName);
        Client client = findClientByName(clientName);

        Buying buying = createBuying(
                vehicule,
                client,
                period
        );

        return saveBuying(buying);
    }

    @Override
    public Page<Buying> getBuyingByClient(
            String clientEmail,
            int page,
            int size) {

        Client client = findClientByEmail(clientEmail);

        Pageable pageable = PageRequest.of(page, size);

        return findBuyingsByClient(client, pageable);
    }

    // -------------------------------------------------------------------------
    // Vehicle
    // -------------------------------------------------------------------------

    private Vehicule findVehiculeByName(String vehiculeName) {

        Vehicule vehicule =
                vehiculeRepository.findVehiculeByNameVehicule(
                        vehiculeName
                );

        if (vehicule == null) {
            throw new RuntimeException("Vehicule not found");
        }

        return vehicule;
    }

    // -------------------------------------------------------------------------
    // Client
    // -------------------------------------------------------------------------

    private Client findClientByName(String clientName) {

        Client client =
                clientRepository.findClientByNameClient(
                        clientName
                );

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        return client;
    }

    private Client findClientByEmail(String clientEmail) {

        Client client =
                clientRepository.findClientByEmail(
                        clientEmail
                );

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        return client;
    }

    // -------------------------------------------------------------------------
    // Buying
    // -------------------------------------------------------------------------

    private Buying createBuying(
            Vehicule vehicule,
            Client client,
            int period) {

        Buying buying = new Buying();

        buying.setDateBuy(Instant.now());
        buying.setPeriodBuy(period);
        buying.setBuyStatus(BuyStatus.BEING_USED);
        buying.setVehicle(vehicule);
        buying.setClient(client);

        return buying;
    }

    private Buying saveBuying(Buying buying) {

        return buyingRepository.save(buying);
    }

    private Page<Buying> findBuyingsByClient(
            Client client,
            Pageable pageable) {

        return buyingRepository.findByClient(
                client,
                pageable
        );
    }
}