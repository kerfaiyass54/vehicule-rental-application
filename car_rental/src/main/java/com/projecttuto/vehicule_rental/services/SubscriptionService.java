package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.SubscriptionDTO;
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
    SubscriptionDTO getSubscription(String emailClient, String nameSupplier);


}
