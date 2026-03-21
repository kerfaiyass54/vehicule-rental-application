package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.LocationDTO;
import com.projecttuto.vehicule_rental.DTO.RepairDTO;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.entities.Vehicule;
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

import com.projecttuto.vehicule_rental.services.RepairService;

import java.util.List;

@RestController
@RequestMapping("/repair")
@CrossOrigin("*")
public class RepairController {

    @Autowired
    private RepairService repairService;

    @PostMapping("/add")
    void addRepair(@RequestBody Repair repair, @RequestParam String location) {
        repairService.addRepair(repair,location);
    }

    @GetMapping("/delete/{id}")
    void deleteRepair(@PathVariable long id){
        repairService.deleteRepair(id);
    }

    @PutMapping("/update")
    void updateRepair(@RequestBody RepairDTO repairDTO){
        repairService.updateRepair(repairDTO);
    }

    @GetMapping("/{nameRepair}/get")
    RepairDTO getRepair(@PathVariable String nameRepair){
        return repairService.getRepair(nameRepair);
    }

    @PostMapping("/pass")
    void changeRepairPassword(@RequestBody Repair repair,@RequestParam String newPassword){
        repairService.changeRepairPassword(repair, newPassword);
    }

    @GetMapping("/tickets/{repairName}")
    List<Ticket> getTickets(@PathVariable String repairName){
        return repairService.getTickets(repairName);
    }

    @GetMapping("/repairinfos/{repairName}")
    List<RepairInfo> getRepairInfo(@PathVariable String repairName){
        return repairService.getRepairInfo(repairName);
    }

    @GetMapping("/vehicules/{repairName}")
    List<Vehicule> getVehicules(@PathVariable String repairName){
        return repairService.getVehicules(repairName);
    }

    @GetMapping("/changeloc/{repairName}")
    void updateLocation(@PathVariable String repairName,@RequestParam String locationName){
        repairService.updateLocation(repairName, locationName);
    }

    @GetMapping("/location/{locationName}")
    LocationDTO getLocation(@PathVariable String locationName){
        return repairService.getLocation(locationName);
    }
}
