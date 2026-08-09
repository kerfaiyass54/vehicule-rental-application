package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;

public interface TicketService {

    Client getClient(String name);
    Repair getRepair(String name);


}
