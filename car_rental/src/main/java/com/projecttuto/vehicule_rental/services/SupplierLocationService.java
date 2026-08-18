package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.LocationDTO;

import java.util.List;

public interface SupplierLocationService {
    public List<String> getLocations(String email);
    public List<String> getCountries(String email);
    public List<LocationDTO> getLocations(String email, int size, int page);

}
