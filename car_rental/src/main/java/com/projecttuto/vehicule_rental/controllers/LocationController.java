package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.entities.Location;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RestController
@RequestMapping("/location")
@CrossOrigin("*")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @PostMapping("/")
    void addLocation(@RequestBody Location location){
        locationService.addLocation(location);
    }


    @GetMapping("/names")
    public ResponseEntity<List<String>> getLocationsNames(){
        List<String> names = locationService.getLocationsNames();
        return ResponseEntity.ok().body(names);
    }


    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountries(){
        List<String> countries = locationService.getCountries();
        return ResponseEntity.ok().body(countries);
    }


    @GetMapping("/{country}/cities")
    public ResponseEntity<List<String>> getCitiesByCountry(
            @PathVariable String country
    ) {

        return ResponseEntity.ok(
                locationService.getCitiesByCountry(country)
        );

    }

    
}
