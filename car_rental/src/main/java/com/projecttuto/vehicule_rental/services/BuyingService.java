package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.data.domain.Page;

public interface BuyingService {
    Buying getBuyingById(long id);
    Vehicule getVehicule(String vehicule);
    Client getClient(String clientName);
    void deleteBuyingById(long id);
    void buyVehicule(String vehiculeName, String clientName, int period);
    void returnVehicule(String vehiculeName, String clientName);
    Buying addBuying(String vehiculeName, String clientName, int period);
    Page<Buying> getBuyingByClient(String clientEmail, int page, int size);

}
