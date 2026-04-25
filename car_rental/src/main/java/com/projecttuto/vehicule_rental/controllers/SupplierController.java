package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.DTO.*;
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
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;

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
    public ResponseEntity<Integer> getSupplierVehicules(
            @PathVariable("email") String email) {

        Integer numberOfVehicules = supplierService.getSupplierVehicules(email);
        return ResponseEntity.ok(numberOfVehicules);
    }


    @GetMapping("/categories/{email}")
    public ResponseEntity<Integer> getSupplierCategories(
            @PathVariable("email") String email) {

        Integer numberOfCategories = supplierService.getSupplierCategories(email);
        return ResponseEntity.ok(numberOfCategories);
    }


    @GetMapping("/addresses/{email}")
    public ResponseEntity<Integer> getSupplierAdresses(
            @PathVariable("email") String email) {

        Integer numberOfAdresses = supplierService.getSupplierAdresses(email);
        return ResponseEntity.ok(numberOfAdresses);
    }


    @GetMapping("/countries/{email}")
    public ResponseEntity<Integer> getSupplierCountries(
            @PathVariable("email") String email) {

        Integer numberOfCountries = supplierService.getSupplierCountries(email);
        return ResponseEntity.ok(numberOfCountries);
    }


    @GetMapping("/locations/{email}")
    public ResponseEntity<Integer> getSupplierLocations(
            @PathVariable("email") String email) {

        Integer numberOfLocations = supplierService.getSupplierLocations(email);
        return ResponseEntity.ok(numberOfLocations);
    }


    @GetMapping("/addresses/list/{email}")
    public ResponseEntity<List<AdressDTO>> getAdressesList(
            @PathVariable("email") String email,
            @RequestParam("size") int size,
            @RequestParam("page") int page) {

        List<AdressDTO> addresses = supplierService.getAdressesList(email, size, page);
        return ResponseEntity.ok(addresses);
    }


    @GetMapping("/countries/list/{email}")
    public ResponseEntity<List<String>> getCountries(
            @PathVariable("email") String email) {

        List<String> countries = supplierService.getCountries(email);
        return ResponseEntity.ok(countries);
    }


    @GetMapping("/locations/list/{email}")
    public ResponseEntity<List<LocationDTO>> getLocations(
            @PathVariable("email") String email,
            @RequestParam("size") int size,
            @RequestParam("page") int page) {

        List<LocationDTO> locations = supplierService.getLocations(email, size, page);
        return ResponseEntity.ok(locations);
    }


    @GetMapping("/vehicules/total/{email}")
    public ResponseEntity<Integer> getTotalVehicules(
            @PathVariable("email") String email) {

        Integer total = supplierService.getTotalVehicules(email);
        return ResponseEntity.ok(total);
    }


    @GetMapping("/vehicules/status/{email}")
    public ResponseEntity<Integer> countVehiculesByStatus(
            @PathVariable("email") String email,
            @RequestParam("status") VehiculeStatus status) {

        Integer count = supplierService
                .countBySupplierEmailAndVehiculeStatus(email, status);

        return ResponseEntity.ok(count);
    }


    @GetMapping("/vehicules/list/{email}")
    public ResponseEntity<List<VehiculeDTO>> getVehiculesList(
            @PathVariable("email") String email) {

        List<VehiculeDTO> vehicules = supplierService.getVehiculesList(email);
        return ResponseEntity.ok(vehicules);
    }


    @PutMapping("/vehicules/update")
    public ResponseEntity<String> updateVehicule(
            @RequestBody VehiculeUpdate vehiculeUpdate) {

        supplierService.updateVehicule(vehiculeUpdate);
        return ResponseEntity.ok("Vehicule updated successfully");
    }


    @PostMapping("/vehicules/add")
    public ResponseEntity<String> addVehicule(
            @RequestBody VehiculeDTO vehiculeDTO) {

        supplierService.addVehicule(vehiculeDTO);
        return ResponseEntity.ok("Vehicule added successfully");
    }


    @GetMapping("/categories/total/{email}")
    public ResponseEntity<Integer> getTotalCategories(
            @PathVariable("email") String email) {

        Integer total = supplierService.getTotalCategories(email);
        return ResponseEntity.ok(total);
    }


    @GetMapping("/categories/stock/{email}")
    public ResponseEntity<Integer> getTotalStock(
            @PathVariable("email") String email) {

        Integer totalStock = supplierService.getTotalStock(email);
        return ResponseEntity.ok(totalStock);
    }


    @GetMapping("/categories/list/{email}")
    public ResponseEntity<List<CategoryDTO>> getCategoryList(
            @PathVariable("email") String email) {

        List<CategoryDTO> categories = supplierService.getCategoryList(email);
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/categories/stock-content/{email}")
    public ResponseEntity<Integer> getStockContent(
            @PathVariable("email") String email,
            @RequestParam("nameCategory") String nameCategory) {

        Integer stock = supplierService.getStockContent(email, nameCategory);
        return ResponseEntity.ok(stock);
    }


    @PostMapping("/categories/add/{email}")
    public ResponseEntity<CategoryDTO> addCategory(
            @RequestBody CategoryDTO categoryDTO,
            @PathVariable("email") String email) {

        CategoryDTO savedCategory = supplierService.addCategory(categoryDTO, email);
        return ResponseEntity.ok(savedCategory);
    }


    @PostMapping("/vehicules/add-new/{email}")
    public ResponseEntity<Vehicule> addVehiculeNew(
            @RequestBody VehiculeCreation vehiculeCreation,
            @PathVariable("email") String email) {

        Vehicule vehicule = supplierService.addVehiculeNew(vehiculeCreation, email);
        return ResponseEntity.ok(vehicule);
    }


    @PostMapping("/addresses/add-new/{email}")
    public ResponseEntity<Adress> addAdressNew(
            @RequestBody AddressCreation addressCreation,
            @PathVariable("email") String email) {

        Adress adress = supplierService.addAdressNew(addressCreation, email);
        return ResponseEntity.ok(adress);
    }


    @GetMapping("/vehicules/names/{email}")
    public ResponseEntity<List<String>> getVehiculesNames(
            @PathVariable("email") String email) {

        List<String> names = supplierService.getVehiculesNames(email);
        return ResponseEntity.ok(names);
    }


    @GetMapping("/categories/names/{email}")
    public ResponseEntity<List<String>> getCategoriesNames(
            @PathVariable("email") String email) {

        List<String> names = supplierService.getCategoriesNames(email);
        return ResponseEntity.ok(names);
    }


    @PutMapping("/addresses/free/{addressId}")
    public ResponseEntity<String> freeAddress(
            @PathVariable("addressId") Long addressId) {

        supplierService.freeAddress(addressId);
        return ResponseEntity.ok("Address freed successfully");
    }






}
