package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.DTO.DemandDTO;
import com.projecttuto.vehicule_rental.entities.Demand;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.services.DemandService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DemandServiceImpl implements DemandService {

    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private RepairRepository repairRepository;

    @Override
    public void addDemand(Demand demand, String supplierName , Ticket ticket) {
        Supplier s = supplierRepository.findSupplierBySuppName(supplierName);
        Vehicule v = ticket.getVehicule();
        demand.setSupplier(s);
        demand.setVehicule(v);
        demandRepository.save(demand);
    }

    @Override
    public void removeDemand(Demand demand){
        demandRepository.delete(demand);
    }

    @Override
    public List<Demand> getDemands(String name){
        if(ifItIsSupplier(name)){
            Supplier s = supplierRepository.findSupplierBySuppName(name);
            return s.getDemands();
        }
        else{
            List<Demand> demands = new ArrayList<>();
            List<Ticket> tickets = ticketRepository.findTicketsByRepair(repairRepository.findRepairByNameRepair(name));
            for(Ticket ticket : tickets){
                demands.addAll(ticket.getDemands());
            }
            return demands;
        }
    }

    @Override
    public DemandDTO getDemand(long id){
        DemandDTO demand = new DemandDTO();
        Demand d = demandRepository.findById(id).get();
        demand.setIdDemand(id);
        demand.setType(d.getType());
        demand.setDateAsk(d.getDateAsk());
        demand.setStatusConfirm(d.getStatusConfirm());
        return demand;
    }

    @Override
    public void updateDemandStatus(Demand demand, ConfirmStatus status){
        Demand d = demandRepository.findById(demand.getIdDemand()).get();
        d.setStatusConfirm(status);
        demandRepository.save(d);
    }

    public boolean ifItIsRepair(String name){
        return repairRepository.findRepairByNameRepair(name)!=null;
    }

    public boolean ifItIsSupplier(String name){
        return supplierRepository.findSupplierBySuppName(name)!=null;
    }





}
