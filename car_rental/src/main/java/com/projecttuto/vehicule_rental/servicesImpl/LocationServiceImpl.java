package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.DTO.LocationDTO;

import com.projecttuto.vehicule_rental.entities.Adress;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.services.LocationService;

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
    public List<Repair> getRepairs(String locationName){
        Location location = locationRepository.findLocationByName(locationName);
        return location.getRepairs();
    }

    @Override
    public List<Supplier> getSuppliers(String locationName){
        Location location = locationRepository.findLocationByName(locationName);
        List<Adress> adresses = location.getAdresses();
        List<Supplier> suppliers = new ArrayList<>();
        for(Adress adress : adresses){
            suppliers.add(adress.getSupplier());
        }
        return suppliers;
    }

    @Override
    public List<Client> getClients(String locationName){

        Location location = locationRepository.findLocationByName(locationName);
        return location.getClients();

    }

    @Override
    public void addLocation(Location location){
        locationRepository.save(location);
    }

    @Override
    public void deleteLocation(String locationName){
        Location location = locationRepository.findLocationByName(locationName);
        if(location.getClients().isEmpty() && location.getRepairs().isEmpty() && location.getAdresses().isEmpty()){
            locationRepository.delete(location);
        }
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
    public List<String> getLocationNamesByCountry(String country){
        return locationRepository.findLocationsByCountry(country).stream().map(Location::getName).toList();
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
