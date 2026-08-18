package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.LocationAdminDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LocationManagementService {
    LocationAdminDTO createLocation(LocationAdminDTO dto);

    Page<LocationAdminDTO> getLocations(int page, int size);

    LocationAdminDTO getLocation(Long id);

    LocationAdminDTO updateLocation(Long id, LocationAdminDTO dto);

    void deleteLocation(Long id);

    List<String> getLocationsNames();

    List<String> getCountries();

    public List<String> getCitiesByCountry(String country);



}
