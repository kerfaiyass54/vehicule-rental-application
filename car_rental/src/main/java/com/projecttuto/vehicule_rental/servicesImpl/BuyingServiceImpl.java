package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.BuyStatus;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Buying getBuyingById(long id){
        return buyingRepository.getById(id);
    }

    @Override
    public Vehicule getVehicule(String vehicule){
        return vehiculeRepository.findVehiculeByNameVehicule(vehicule);
    }

    @Override
    public Client getClient(String clientName){
        return clientRepository.findClientByNameClient( clientName );
    }

    @Override
    public void deleteBuyingById(long id){
        buyingRepository.deleteById(id);
    }


    @Override
    public void returnVehicule(String vehiculeName, String clientName){
        Vehicule vehicule = vehiculeRepository.findVehiculeByNameVehicule(vehiculeName);
        Client client = clientRepository.findClientByNameClient(clientName);
        List<Buying> buyings = buyingRepository.findAll();
        for(Buying buying : buyings){
            if(buying.getClient().equals(client) && buying.getVehicule().equals(vehicule) && vehicule.getVehiculeStatus().equals(VehiculeStatus.TAKEN)){
                buying.setBuyStatus(BuyStatus.FINISHED);
                vehicule.setVehiculeStatus(VehiculeStatus.AVAILABLE);
                vehiculeRepository.save(vehicule);
                buyingRepository.save(buying);
                break;
            }
        }
    }

    @Override
    public void buyVehicule(String vehiculeName, String clientName, int period){
        Vehicule vehicule = vehiculeRepository.findVehiculeByNameVehicule(vehiculeName);
        Client client = clientRepository.findClientByNameClient(clientName);
        double payment = vehicule.getPrice() * period;
        if(vehicule.getVehiculeStatus() == VehiculeStatus.AVAILABLE && payment < client.getBudget()){
            Buying buying = new Buying();
            client.setBudget(client.getBudget() - payment);
            vehicule.setVehiculeStatus(VehiculeStatus.TAKEN);
            buying.setClient(client);
            buying.setVehicule(vehicule);
            buying.setPeriodBuy(period);
            buying.setDateBuy(Instant.now());
            buying.setBuyStatus(BuyStatus.BEING_USED);
            buyingRepository.save(buying);
            clientRepository.save(client);
            vehiculeRepository.save(vehicule);
        }
    }

}
