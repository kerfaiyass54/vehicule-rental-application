package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.LocationAdminDTO;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import com.projecttuto.vehicule_rental.services.LocationManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationManagementServiceImpl implements LocationManagementService {

    private final LocationRepository locationRepository;

    @Override
    public List<String> getLocationsNames(){
        List<String> locationNames = new ArrayList<>();
        List<Location> locations = locationRepository.findAll();
        for(Location location : locations){
            locationNames.add(location.getLocationName());
        }
        return locationNames;
    }

    @Override
    public List<String> getCountries(){
        return locationRepository.findAll().stream().map(Location::getCountry).toList();
    }


    @Override
    public List<String> getCitiesByCountry(String country) {

        return locationRepository
                .findLocationsByCountry(country).stream().map(Location::getLocationName).toList();

    }

    @Override
    public LocationAdminDTO createLocation(LocationAdminDTO dto) {

        Location location = new Location();

        location.setLocationName(dto.getName());

        location.setCountry(dto.getCountry());

        location.setPosition(dto.getPosition());

        Location saved = locationRepository.save(location);

        LocationAdminDTO response = new LocationAdminDTO();

        response.setId(saved.getIdLocation());

        response.setName(saved.getLocationName());

        response.setCountry(saved.getCountry());

        response.setPosition(saved.getPosition());

        return response;
    }



    @Override
    public Page<LocationAdminDTO> getLocations(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return locationRepository.findAll(pageable)
                .map(location -> {

                    LocationAdminDTO dto = new LocationAdminDTO();

                    dto.setId(location.getIdLocation());

                    dto.setName(location.getLocationName());

                    dto.setCountry(location.getCountry());

                    dto.setPosition(location.getPosition());

                    return dto;
                });
    }

    @Override
    public LocationAdminDTO getLocation(Long id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Location not found"));

        LocationAdminDTO dto = new LocationAdminDTO();

        dto.setId(location.getIdLocation());

        dto.setName(location.getLocationName());

        dto.setCountry(location.getCountry());

        dto.setPosition(location.getPosition());

        return dto;
    }

    @Override
    public LocationAdminDTO updateLocation(Long id,
                                           LocationAdminDTO dto) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Location not found"));

        location.setLocationName(dto.getName());

        location.setCountry(dto.getCountry());

        location.setPosition(dto.getPosition());

        Location saved = locationRepository.save(location);

        LocationAdminDTO response = new LocationAdminDTO();

        response.setId(saved.getIdLocation());

        response.setName(saved.getLocationName());

        response.setCountry(saved.getCountry());

        response.setPosition(saved.getPosition());

        return response;
    }

    @Override
    public void deleteLocation(Long id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Location not found"));

        locationRepository.delete(location);

    }

}
