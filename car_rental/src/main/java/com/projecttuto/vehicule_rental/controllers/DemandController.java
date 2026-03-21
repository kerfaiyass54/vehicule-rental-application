package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.DemandDTO;
import com.projecttuto.vehicule_rental.entities.Demand;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projecttuto.vehicule_rental.services.DemandService;

import java.util.List;

@RestController
@RequestMapping("/demand")
@CrossOrigin("*")
public class DemandController {

    @Autowired
    private DemandService demandService;

    @PostMapping("/add")
    void addDemand(@RequestBody Demand demand,@RequestParam String supplierName ,@RequestParam Ticket ticket){
        demandService.addDemand(demand, supplierName, ticket);
    }

    @DeleteMapping("/delete")
    void removeDemand(@RequestBody Demand demand){
        demandService.removeDemand(demand);
    }

    @GetMapping("/demands/{name}")
    List<Demand> getDemands(@PathVariable String name){
        return demandService.getDemands(name);
    }

    @GetMapping("/get/{id}")
    DemandDTO getDemand(@PathVariable long id){
        return demandService.getDemand(id);
    }

    @PostMapping("/update")
    void updateDemandStatus(@RequestBody Demand demand,@RequestParam ConfirmStatus status){
        demandService.updateDemandStatus(demand, status);
    }
}
