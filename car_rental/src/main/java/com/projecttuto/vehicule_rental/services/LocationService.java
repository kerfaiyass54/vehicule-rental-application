package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.LocationDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;

import java.util.List;

public interface LocationService {
    List<Repair> getRepairs(String locationName);
    List<Supplier> getSuppliers(String locationName);
    List<Client> getClients(String locationName);
    void addLocation(Location location);
    void deleteLocation(String locationName);
    LocationDTO getLocation(String locationName);
    List<String> getLocationNamesByCountry(String country);
    List<String> getLocationsNames();

}
