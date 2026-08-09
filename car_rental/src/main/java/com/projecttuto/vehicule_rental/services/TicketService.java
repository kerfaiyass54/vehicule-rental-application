package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.StatusRepair;

public interface TicketService {

    Client getClient(String name);
    Repair getRepair(String name);


}
