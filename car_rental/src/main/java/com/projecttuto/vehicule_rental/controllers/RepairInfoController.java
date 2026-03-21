package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.RepairDTO;
import com.projecttuto.vehicule_rental.DTO.RepairInfoDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeDTO;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
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
import com.projecttuto.vehicule_rental.services.RepairInfoService;

import java.time.Instant;

@RestController
@RequestMapping("/repairinfo")
@CrossOrigin("*")
public class RepairInfoController {

    @Autowired
    private RepairInfoService repairInfoService;

    @PostMapping("/date")
    void updateDateStart(@RequestParam Instant date,@RequestBody RepairInfo repairInfo){
        repairInfoService.updateDateStart(date, repairInfo);
    }

    @PostMapping("/status")
    void updateStatus(@RequestBody RepairInfo repairInfo,@RequestParam RepairStatus repairStatus){
        repairInfoService.updateStatus(repairInfo, repairStatus);
    }

    @GetMapping("/get/{id}")
    RepairInfoDTO getRepairInfoById(@PathVariable long id){
        return repairInfoService.getRepairInfoById(id);
    }

    @GetMapping("/vehicule/{name}")
    VehiculeDTO getVehiculeById(@PathVariable String name){
        return repairInfoService.getVehiculeById(name);
    }

    @GetMapping("/repair/{name}")
    RepairDTO getRepair(@PathVariable String name){
        return repairInfoService.getRepair(name);
    }

    @PostMapping("/add/{vehiculeName}")
    void addReparation(@PathVariable String vehiculeName,@RequestParam String repairName,@RequestParam Instant date){
        repairInfoService.addReparation(vehiculeName, repairName, date);
    }


}
