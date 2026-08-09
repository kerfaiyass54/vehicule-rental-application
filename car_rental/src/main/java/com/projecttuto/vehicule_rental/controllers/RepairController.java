package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.CreateDemandDTO;
import com.projecttuto.vehicule_rental.DTO.RepairDashboardDTO;
import com.projecttuto.vehicule_rental.DTO.RepairInfoDTO;
import com.projecttuto.vehicule_rental.DTO.RepairProfileDTO;
import com.projecttuto.vehicule_rental.DTO.RepairTicketDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.projecttuto.vehicule_rental.services.RepairService;

@RestController
@RequestMapping("/repair")
@CrossOrigin("*")
public class RepairController {

    private final RepairService repairService;

    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }



    @PostMapping("/demand")
    public ResponseEntity<RepairTicketDTO> createDemand(
            @RequestBody CreateDemandDTO dto) {

        return ResponseEntity.ok(
                repairService.createDemand(dto));
    }

    @PostMapping("/{ticketId}")
    public ResponseEntity<RepairInfoDTO> startRepair(
            @PathVariable Long ticketId) {

        return ResponseEntity.ok(
                repairService.startRepair(ticketId));
    }

    @GetMapping("/{email}/repairs")
    public ResponseEntity<Page<RepairInfoDTO>> checkRepairs(

            @PathVariable String email,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        return ResponseEntity.ok(
                repairService.checkRepairs(email, page, size));

    }

    @GetMapping("/{email}/profile")
    public ResponseEntity<RepairProfileDTO> getInfo(
            @PathVariable String email) {

        return ResponseEntity.ok(
                repairService.getInfo(email));
    }

    @PatchMapping("/{email}/location")
    public ResponseEntity<RepairProfileDTO> updateLocation(
            @PathVariable String email,
            @RequestParam Long locationId) {

        return ResponseEntity.ok(
                repairService.updateLocation(email, locationId));
    }

    @GetMapping("/{email}/dashboard")
    public ResponseEntity<RepairDashboardDTO> getDashboard(
            @PathVariable String email) {

        return ResponseEntity.ok(
                repairService.getDashboard(email));
    }


    @PatchMapping("/{repairInfoId}/cancel")
    public ResponseEntity<String> cancelRepair(
            @PathVariable Long repairInfoId) {

        repairService.cancelRepair(repairInfoId);

        return ResponseEntity.ok("Repair cancelled successfully.");
    }

    @GetMapping("/{email}/tickets")
    public ResponseEntity<Page<RepairTicketDTO>> getTickets(

            @PathVariable String email,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                repairService.getTickets(email, page, size));
    }


    @GetMapping("/{email}/repair-infos")
    public ResponseEntity<Page<RepairInfoDTO>> getRepairInfos(

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


}
