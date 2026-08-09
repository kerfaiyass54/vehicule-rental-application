package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.BuyStatus;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.repositories.BuyingRepository;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.BuyingService;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BuyingServiceImpl implements BuyingService {

    @Autowired
    private BuyingRepository buyingRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private ClientRepository clientRepository;



    @Override
    public Client getClient(String clientName){
        return clientRepository.findClientByNameClient( clientName );
    }



    @Override
    public Buying addBuying(String vehiculeName, String clientName, int period) {

        Vehicule vehicule = vehiculeRepository.findVehiculeByNameVehicule(vehiculeName);
        if (vehicule == null) {
            throw new RuntimeException("Vehicule not found");
        }

        Client client = clientRepository.findClientByNameClient(clientName);
        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Buying buying = new Buying();
        buying.setDateBuy(Instant.now());
        buying.setPeriodBuy(period);
        buying.setBuyStatus(BuyStatus.BEING_USED); // or another default status
        buying.setVehicule(vehicule);
        buying.setClient(client);

        return buyingRepository.save(buying);
    }

    @Override
    public Page<Buying> getBuyingByClient(String clientEmail, int page, int size) {

        Client client = clientRepository.findClientByEmail(clientEmail);
        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Pageable pageable = PageRequest.of(page, size);

        return buyingRepository.findByClient(client, pageable);
    }

}
