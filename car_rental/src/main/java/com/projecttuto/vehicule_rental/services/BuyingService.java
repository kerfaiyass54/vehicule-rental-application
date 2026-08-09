package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.data.domain.Page;

public interface BuyingService {

    Client getClient(String clientName);

    Buying addBuying(String vehiculeName, String clientName, int period);
    Page<Buying> getBuyingByClient(String clientEmail, int page, int size);

}
