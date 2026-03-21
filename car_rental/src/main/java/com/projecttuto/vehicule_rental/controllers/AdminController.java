package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.projecttuto.vehicule_rental.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;






import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/clients/{id}")
    public ResponseEntity<List<ClientDTO>> getClients(@PathVariable long id){
        List<ClientDTO> clients =  adminService.getClients(id);
        if (clients != null) {
            return ResponseEntity.ok(clients);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/suppliers/{id}")
    public ResponseEntity<List<SupplierDTO>> getSuppliers(@PathVariable long id){
        List<SupplierDTO> suppliers =  adminService.getSuppliers(id);
        if (suppliers != null) {
            return ResponseEntity.ok(suppliers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/repairs/{id}")
    public ResponseEntity<List<RepairDTO>> getRepairs(@PathVariable long id){
        List<RepairDTO> repairs =  adminService.getRepairs(id);
        if (repairs != null) {
            return ResponseEntity.ok(repairs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<List<LocationDTO>> getLocations(@PathVariable long id){
        List<LocationDTO> locations =  adminService.getLocations(id);
        if (locations != null) {
            return ResponseEntity.ok(locations);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDetails(@RequestBody AdminDTO adminDTO,@PathVariable long id){
        adminService.updateDetails(adminDTO,id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{adminName}")
    public ResponseEntity<AdminDTO> getDetails(@PathVariable long id){
        AdminDTO admin =  adminService.getDetails(id);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getNames(){
        List<String> names =  adminService.getNames();
        if (names != null) {
            return ResponseEntity.ok(names);
        } else {
            return ResponseEntity.notFound().build();
        }
    }






}
