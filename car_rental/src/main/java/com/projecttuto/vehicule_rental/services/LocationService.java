package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.LocationDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;

import java.util.List;

public interface LocationService {

    void addLocation(Location location);
    LocationDTO getLocation(String locationName);
    List<String> getLocationsNames();
    List<String> getCountries();
    public List<String> getCitiesByCountry(String country);

}
