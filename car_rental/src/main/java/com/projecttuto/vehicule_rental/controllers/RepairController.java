package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.*;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/dashboard/{email}")
    public ResponseEntity<RepairDashboardDTO> dashboard(
            @PathVariable String email){

        return ResponseEntity.ok(
                repairService.getDashboard(email));
    }

    @PostMapping("/demands")
    public ResponseEntity<RepairTicketDTO> createDemand(
            @RequestBody CreateDemandDTO dto) {

        return ResponseEntity.ok(
                repairService.createDemand(dto));
    }

    @PostMapping("/start/{ticketId}")
    public ResponseEntity<RepairInfoDTO> startRepair(
            @PathVariable Long ticketId) {

        return ResponseEntity.ok(
                repairService.startRepair(ticketId));
    }

    @GetMapping("/repairs/{email}")
    public ResponseEntity<Page<RepairInfoDTO>> checkRepairs(

            @PathVariable String email,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        return ResponseEntity.ok(
                repairService.checkRepairs(email, page, size));

    }

    @GetMapping("/profile/{email}")
    public ResponseEntity<RepairProfileDTO> getInfo(
            @PathVariable String email) {

        return ResponseEntity.ok(
                repairService.getInfo(email));
    }

    @PutMapping("/location/{email}/{locationId}")
    public ResponseEntity<RepairProfileDTO> updateLocation(
            @PathVariable String email,
            @PathVariable Long locationId) {

        return ResponseEntity.ok(
                repairService.updateLocation(email, locationId));
    }

    @GetMapping("/dashboard/{email}")
    public ResponseEntity<RepairDashboardDTO> getDashboard(
            @PathVariable String email) {

        return ResponseEntity.ok(
                repairService.getDashboard(email));
    }


    @PutMapping("/cancel/{repairInfoId}")
    public ResponseEntity<String> cancelRepair(
            @PathVariable Long repairInfoId) {

        repairService.cancelRepair(repairInfoId);

        return ResponseEntity.ok("Repair cancelled successfully.");
    }

    @GetMapping("/tickets/{email}")
    public ResponseEntity<Page<RepairTicketDTO>> getTickets(

            @PathVariable String email,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                repairService.getTickets(email, page, size));
    }


    @GetMapping("/infos/{email}")
    public ResponseEntity<Page<RepairInfoDTO>> infos(

            @PathVariable String email,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size){

        return ResponseEntity.ok(

                repairService.getRepairInfos(
                        email,
                        page,
                        size));
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
