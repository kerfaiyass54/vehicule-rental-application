package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.SubscripionInfoDTO;
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
    public Supplier getSupplier(String name){
        return supplierRepository.findSupplierBySuppName(name);
    }



}
