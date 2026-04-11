package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.DTO.LocationDTO;
import com.projecttuto.vehicule_rental.DTO.SupplierDTO;
import com.projecttuto.vehicule_rental.DTO.SupplierDetailsDTO;
import com.projecttuto.vehicule_rental.entities.Adress;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/supplier")
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

    @GetMapping("/details/{email}")
    public ResponseEntity<SupplierDetailsDTO> getSupplierDetails(@PathVariable("email") String email){
        SupplierDetailsDTO supplierDetailsDTO = supplierService.getDetails(email);
        return ResponseEntity.ok().body(supplierDetailsDTO);
    }

    @GetMapping("/vehicules/{email}")
    public ResponseEntity<Integer> getSupplierVehicules(@PathVariable String email){
        Integer numberOfVehicules = supplierService.getSupplierVehicules(email);
        return ResponseEntity.ok().body(numberOfVehicules);
    }

    @GetMapping("/categories/{email}")
    public ResponseEntity<Integer> getSupplierCategories(@PathVariable String email){
        Integer numberOfCategories = supplierService.getSupplierCategories(email);
        return ResponseEntity.ok().body(numberOfCategories);
    }

    @GetMapping("/addresses/{email}")
    public ResponseEntity<Integer> getSupplierAdresses(@PathVariable String email){
        Integer numberOfAdresses = supplierService.getSupplierAdresses(email);
        return ResponseEntity.ok().body(numberOfAdresses);
    }

    @GetMapping("/countries/{email}")
    public ResponseEntity<Integer> getSupplierCountries(@PathVariable String email){
        Integer numberOfCountries = supplierService.getSupplierCountries(email);
        return ResponseEntity.ok().body(numberOfCountries);
    }

    @GetMapping("/locations/{email}")
    public ResponseEntity<Integer> getSupplierLocations(@PathVariable String email){
        Integer numberOfLocations = supplierService.getSupplierLocations(email);
        return ResponseEntity.ok().body(numberOfLocations);
    }

    @GetMapping("/addresses/list/{email}")
    public ResponseEntity<List<String>> getAdressesList(
            @PathVariable String email,
            @RequestParam int size,
            @RequestParam int page) {

        List<String> addresses = supplierService.getAdressesList(email, size, page);
        return ResponseEntity.ok().body(addresses);
    }


    @GetMapping("/countries/list/{email}")
    public ResponseEntity<List<String>> getCountries(@PathVariable String email) {

        List<String> countries = supplierService.getCountries(email);
        return ResponseEntity.ok().body(countries);
    }


    @GetMapping("/locations/list/{email}")
    public ResponseEntity<List<LocationDTO>> getLocations(
            @PathVariable String email,
            @RequestParam int size,
            @RequestParam int page) {

        List<LocationDTO> locations = supplierService.getLocations(email, size, page);
        return ResponseEntity.ok().body(locations);
    }



}
