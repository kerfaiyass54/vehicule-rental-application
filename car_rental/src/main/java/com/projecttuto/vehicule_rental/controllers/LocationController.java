package com.projecttuto.vehicule_rental.controllers;
import com.projecttuto.vehicule_rental.DTO.LocationDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.projecttuto.vehicule_rental.services.LocationService;

import java.util.List;

@RestController
@RequestMapping("/location")
@CrossOrigin("*")
public class LocationController {

    @Autowired
    private LocationService locationService;


    @GetMapping("/repairs/{locationName}")
    List<Repair> getRepairs(@PathVariable String locationName){
        return locationService.getRepairs(locationName);
    }

    @GetMapping("/suppliers/{locationName}")
    List<Supplier> getSuppliers(@PathVariable String locationName){
        return locationService.getSuppliers(locationName);
    }

    @GetMapping("/clients/{locationName}")
    List<Client> getClients(@PathVariable String locationName){
        return locationService.getClients(locationName);
    }

    @PostMapping("/add")
    void addLocation(@RequestBody Location location){
        locationService.addLocation(location);
    }

    @GetMapping("/delete/{locationName}")
    void deleteLocation(@PathVariable String locationName){
        locationService.deleteLocation(locationName);
    }

    @GetMapping("/{locationName}/get")
    LocationDTO getLocation(@PathVariable String locationName){
        return locationService.getLocation(locationName);
    }

    @GetMapping("/names/{country}")
    List<String> getLocationNamesByCountry(@PathVariable String country){
        return locationService.getLocationNamesByCountry(country);
    }

    @GetMapping("/locNames")
    List<String> getLocationsNames(){
        return locationService.getLocationsNames();
    }

    
}
