package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.DTO.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;

public interface SubscriptionService {

    void addSubscription(Subscription subscription, String emailClient, String nameSupplier);
    void removeSubscription(Subscription subscription);
    void renewSubscription(Subscription subscription, String nameClient, int time);
    Client getClientByEmail(String email);
    Supplier getSupplier(String name);
    boolean isClientSubscript(String emailClient, String nameSupplier);
    SubscripionInfoDTO getSubscription(String emailClient, String nameSupplier);
    SubscripionInfoDTO addSubscription(SubscripionInfoDTO dto);

    SubscripionInfoDTO renewSubscription(String clientEmail);

    void cancelSubscription(String clientEmail);


}
