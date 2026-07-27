package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.DTO.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.DTO.SubscripionInfoDTO;
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

import java.time.Instant;

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
    public void cancelSubscription(String clientEmail) {

        Client client = clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Subscription subscription = subscriptionRepository.findByClient(client)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscriptionRepository.delete(subscription);
    }

    @Override
    public SubscripionInfoDTO renewSubscription(String clientEmail) {

        Client client = clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Subscription subscription = subscriptionRepository.findByClient(client)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setDateStart(Instant.now());

        Subscription updated = subscriptionRepository.save(subscription);

        SubscripionInfoDTO dto = new SubscripionInfoDTO();

        dto.setIdSubscrip(updated.getIdSubscrip());
        dto.setType(updated.getType());
        dto.setDateStart(updated.getDateStart());
        dto.setPrice(updated.getPrice());
        dto.setReduce(updated.getReduce());
        dto.setSupplierName(updated.getSupplier().getSuppName());
        dto.setClientEmail(updated.getClient().getEmail());

        return dto;
    }



    @Override
    public SubscripionInfoDTO addSubscription(SubscripionInfoDTO dto) {

        Client client = clientRepository.findClientByEmail(dto.getClientEmail());

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Supplier supplier = supplierRepository.findSupplierBySuppName(dto.getSupplierName());

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        if (subscriptionRepository.findByClient(client).isPresent()) {
            throw new RuntimeException("Client already has a subscription.");
        }

        Subscription subscription = new Subscription();

        subscription.setType(dto.getType());
        subscription.setDateStart(Instant.now());
        subscription.setSupplier(supplier);
        subscription.setClient(client);

        switch (dto.getType()) {

            case BASIC:
                subscription.setPrice(100);
                subscription.setReduce(5);
                break;

            case PREMIUM:
                subscription.setPrice(250);
                subscription.setReduce(20);
                break;

            case MONTHLY:
                subscription.setPrice(40);
                subscription.setReduce(10);
                break;

            case ANNUAL:
                subscription.setPrice(400);
                subscription.setReduce(30);
                break;
        }

        Subscription saved = subscriptionRepository.save(subscription);

        SubscripionInfoDTO response = new SubscripionInfoDTO();

        response.setIdSubscrip(saved.getIdSubscrip());
        response.setType(saved.getType());
        response.setDateStart(saved.getDateStart());
        response.setPrice(saved.getPrice());
        response.setReduce(saved.getReduce());
        response.setSupplierName(saved.getSupplier().getSuppName());
        response.setClientEmail(saved.getClient().getEmail());

        return response;
    }

    

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
    public SubscripionInfoDTO getSubscription(String emailClient, String nameSupplier){
        SubscripionInfoDTO subscriptionDTO = new SubscripionInfoDTO();
        Subscription subscription = subscriptionRepository.findSubscriptionByClientAndSupplier(clientRepository.findClientByEmail(emailClient),supplierRepository.findSupplierBySuppName(nameSupplier));
        
        subscriptionDTO.setDateStart(subscription.getDateStart());
        subscriptionDTO.setType(subscription.getType());
        subscriptionDTO.setPrice(subscription.getPrice());
        
        subscriptionDTO.setReduce(subscription.getReduce());
        return subscriptionDTO;
    }

}
