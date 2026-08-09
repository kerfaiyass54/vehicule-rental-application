package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.DTO.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;

public interface SubscriptionService {


    Supplier getSupplier(String name);

    SubscripionInfoDTO addSubscription(SubscripionInfoDTO dto);

    SubscripionInfoDTO renewSubscription(String clientEmail);

    void cancelSubscription(String clientEmail);


}
