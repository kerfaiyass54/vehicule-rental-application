package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.LocationDTO;
import com.projecttuto.vehicule_rental.entities.Location;

import java.util.List;

public interface LocationService {

    void addLocation(Location location);
    LocationDTO getLocation(String locationName);
    List<String> getLocationsNames();
    List<String> getCountries();
    public List<String> getCitiesByCountry(String country);

}
