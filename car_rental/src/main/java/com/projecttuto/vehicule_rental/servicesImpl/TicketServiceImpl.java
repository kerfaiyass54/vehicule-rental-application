package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.StatusRepair;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.DemandRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.TicketRepository;
import com.projecttuto.vehicule_rental.services.TicketService;

@Service
@AllArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private DemandRepository demandRepository;

    @Override
    public Client getClient(String name){
        return clientRepository.findClientByNameClient(name );
    }
    @Override
    public Repair getRepair(String name){
        return repairRepository.findRepairByNameRepair(name );
    }

    @Override
    public void changeStatus(long id, StatusRepair status){
        Ticket ticket = ticketRepository.getById(id);
        ticket.setStatus(status);
        ticketRepository.save(ticket);
    }

    @Override
    public void addTicket(Ticket ticket, String repairName, String clientName){
        Client client = clientRepository.findClientByNameClient(clientName);
        Repair repair = repairRepository.findRepairByNameRepair(repairName);
        if(client != null && repair != null){
            ticket.setClient(client);
            ticket.setRepair(repair);
            ticketRepository.save(ticket);
        }
    }

    @Override
    public void removeTicket(Ticket ticket){}
}
