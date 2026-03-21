package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.StatusRepair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;

import com.projecttuto.vehicule_rental.services.TicketService;

@RestController
@RequestMapping("/ticket")
@CrossOrigin("*")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/client/{name}")
    public Client getClient(@PathVariable String name){
        return ticketService.getClient(name);
    }

    @GetMapping("/repair/{name}")
    public Repair getRepair(@PathVariable String name){
        return ticketService.getRepair(name);
    }

    @GetMapping("/status/{id}")
    public void changeStatus(@PathVariable long id, @RequestParam StatusRepair status){
        ticketService.changeStatus(id, status);
    }

    @GetMapping("/ticket/{repairName}")
    public void addTicket(@RequestBody Ticket ticket,@PathVariable String repairName,@RequestParam String clientName){
        ticketService.addTicket(ticket, repairName, clientName);
    }

    @GetMapping("/remove")
    public void removeTicket(@RequestBody Ticket ticket){
        ticketService.removeTicket(ticket);
    }
}
