package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.DTO.SubscriptionDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.SubscriptionRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.SubscriptionService;

@Service
@AllArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public void addSubscription(Subscription subscription, String emailClient, String nameSupplier){
        Client client = clientRepository.findClientByEmail(emailClient);
        subscription.setClient(client);
        subscription.setSupplier(supplierRepository.findSupplierBySuppName(nameSupplier));
        client.setBudget(client.getBudget() - subscription.getPrice());
        clientRepository.save(client);
        subscriptionRepository.save(subscription);
    }
    @Override
    public void removeSubscription(Subscription subscription){
        subscriptionRepository.delete(subscription);
    }
    @Override
    public void renewSubscription(Subscription subscription, String nameClient, int time){

    }
    @Override
    public Client getClientByEmail(String email){
        return clientRepository.findClientByEmail(email);
    }
    @Override
    public Supplier getSupplier(String name){
        return supplierRepository.findSupplierBySuppName(name);
    }

    @Override
    public boolean isClientSubscript(String emailClient, String nameSupplier){
        return subscriptionRepository.findSubscriptionByClientAndSupplier(clientRepository.findClientByEmail(emailClient), supplierRepository.findSupplierBySuppName(nameSupplier)) != null;
    }

    @Override
    public SubscriptionDTO getSubscription(String emailClient, String nameSupplier){
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        Subscription subscription = subscriptionRepository.findSubscriptionByClientAndSupplier(clientRepository.findClientByEmail(emailClient),supplierRepository.findSupplierBySuppName(nameSupplier));
        
        subscriptionDTO.setDateStart(subscription.getDateStart());
        subscriptionDTO.setType(subscription.getType());
        subscriptionDTO.setPrice(subscription.getPrice());
        
        subscriptionDTO.setReduce(subscription.getReduce());
        return subscriptionDTO;
    }

}
