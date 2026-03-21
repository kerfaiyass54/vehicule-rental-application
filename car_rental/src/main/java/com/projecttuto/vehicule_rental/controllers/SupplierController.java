package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.DTO.SupplierDTO;
import com.projecttuto.vehicule_rental.entities.Adress;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projecttuto.vehicule_rental.services.SupplierService;

import java.util.List;

@RestController
@RequestMapping("/supp")
@CrossOrigin("*")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping("/add")
    public void addSupplier(@RequestBody Supplier supplier){
        supplierService.addSupplier(supplier);
    }

    @PutMapping("/update")
    public void updateSupplier(@RequestBody SupplierDTO supplierDTO){
        supplierService.updateSupplier(supplierDTO);
    }

    @GetMapping("/delete/{name}")
    public void deleteSupplier(@PathVariable String name){
        supplierService.deleteSupplier(name);
    }

    @GetMapping("/supplier/{supplierName}")
    public SupplierDTO getSupplier(@PathVariable("supplierName") String supplierName){
        return supplierService.getSupplier(supplierName);
    }

    @PostMapping("/password")
    public void changeSupplierPassword(@RequestBody Supplier supplier,@RequestParam String newPassword){
        supplierService.changeSupplierPassword(supplier,newPassword);
    }

    @GetMapping("/subs")
    public List<Subscription> getSubscriptions(Supplier supplier){
        return supplierService.getSubscriptions(supplier);
    }

    @GetMapping("/adr")
    public List<Adress> getAdresses(Supplier supplier){
        return supplierService.getAdresses(supplier);
    }

    @GetMapping("/veh")
    public List<Vehicule> getVehicules(Supplier supplier){
        return supplierService.getVehicules(supplier);
    }


}
