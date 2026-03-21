package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Vehicule;

public interface BuyingService {
    Buying getBuyingById(long id);
    Vehicule getVehicule(String vehicule);
    Client getClient(String clientName);
    void deleteBuyingById(long id);
    void buyVehicule(String vehiculeName, String clientName, int period);
    void returnVehicule(String vehiculeName, String clientName);
}
