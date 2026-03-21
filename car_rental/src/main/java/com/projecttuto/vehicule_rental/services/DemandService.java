package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.DemandDTO;
import com.projecttuto.vehicule_rental.entities.Demand;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;

import java.util.List;

public interface DemandService {
    void addDemand(Demand demand, String supplierName , Ticket ticket);
    void removeDemand(Demand demand);
    List<Demand> getDemands(String name);
    DemandDTO getDemand(long id);
    void updateDemandStatus(Demand demand, ConfirmStatus status);
}
