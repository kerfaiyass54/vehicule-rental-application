package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.StatusRepair;

public interface TicketService {

    Client getClient(String name);
    Repair getRepair(String name);
    void changeStatus(long id, StatusRepair status);
    void addTicket(Ticket ticket, String repairName, String clientName);
    void removeTicket(Ticket ticket);


}
