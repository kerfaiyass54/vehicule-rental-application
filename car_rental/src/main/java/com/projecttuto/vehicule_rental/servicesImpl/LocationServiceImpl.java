package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.LocationDTO;

import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }



    @Override
    public void addLocation(Location location){
        locationRepository.save(location);
    }


    @Override
    public LocationDTO getLocation(String locationName){
        Location location = locationRepository.findLocationByName(locationName);
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCountry(location.getCountry());
        locationDTO.setName(location.getName());
        locationDTO.setIdLoc(location.getIdLoc());
        return locationDTO;
    }


    @Override
    public List<String> getLocationsNames(){
        List<String> locationNames = new ArrayList<>();
        List<Location> locations = locationRepository.findAll();
        for(Location location : locations){
            locationNames.add(location.getName());
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
                .findLocationsByCountry(country).stream().map(Location::getName).toList();

    }




}
