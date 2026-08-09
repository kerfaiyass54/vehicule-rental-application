package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.RepairInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.projecttuto.vehicule_rental.services.RepairInfoService;

@RestController
@RequestMapping("/repairinfo")
@CrossOrigin("*")
public class RepairInfoController {


    // replace this one in the repair controller and delete this

    @Autowired
    private RepairInfoService repairInfoService;


    @GetMapping("/get/{id}")
    RepairInfoDTO getRepairInfoById(@PathVariable Long id){
        return repairInfoService.getRepairInfoById(id);
    }



}
