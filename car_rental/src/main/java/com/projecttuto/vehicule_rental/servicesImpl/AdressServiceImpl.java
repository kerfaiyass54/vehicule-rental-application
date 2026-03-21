package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.DTO.AdressDTO;
import com.projecttuto.vehicule_rental.DTO.AdressSupplierDTO;
import com.projecttuto.vehicule_rental.entities.Adress;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.enums.AdressStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.repositories.AdressRepository;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.AdressService;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class AdressServiceImpl implements AdressService {

    private final AdressRepository adressRepository;

    private final SupplierRepository supplierRepository;

    private final LocationRepository locationRepository;

    public AdressServiceImpl(AdressRepository adressRepository, SupplierRepository supplierRepository,
                             LocationRepository locationRepository) {
        this.adressRepository = adressRepository;
        this.supplierRepository = supplierRepository;
        this.locationRepository = locationRepository;
    }

    public AdressDTO getAdressDTO(Adress adress) {
        AdressDTO adressDTO = new AdressDTO();
        adressDTO.setIdAdress(adress.getIdAdress());
        adressDTO.setRoad(adress.getRoad());
        adressDTO.setNumber(adress.getNumber());
        adressDTO.setAdressStatus(adress.getAdressStatus());
        adressDTO.setSupplier(adress.getSupplier().getSuppName());
        adressDTO.setLocation(adress.getLocation().getName());
        return adressDTO;
    }

    public AdressSupplierDTO getAdressSupplierDTO(Adress adress) {
        AdressSupplierDTO adressSupplierDTO = new AdressSupplierDTO();
        adressSupplierDTO.setIdAdress(adress.getIdAdress());
        adressSupplierDTO.setRoad(adress.getRoad());
        adressSupplierDTO.setNumber(adress.getNumber());
        adressSupplierDTO.setLocation(adress.getLocation().getName());
        return adressSupplierDTO;
    }


    @Override
    public AdressDTO addAddressToSupplier(AdressDTO adressDTO) {
        Adress adress = new Adress();
        adress.setRoad(adressDTO.getRoad());
        adress.setNumber(adressDTO.getNumber());
        adress.setAdressStatus(adressDTO.getAdressStatus());
        adress.setSupplier(supplierRepository.findSupplierBySuppName(adressDTO.getSupplier()));
        adress.setLocation(locationRepository.findLocationByName(adressDTO.getLocation()));
        Adress adressSaved = adressRepository.save(adress);
        return getAdressDTO(adressSaved);
    }

    @Override
    public Page<AdressSupplierDTO> getSuppliersAdresses(int page, int size, String supplierName) {
        Pageable pageable = PageRequest.of(page, size);
        Supplier supplier = supplierRepository.findSupplierBySuppName(supplierName);
        return adressRepository.findAdressesBySupplier(supplier,pageable).map(this::getAdressSupplierDTO);
    }

    @Override
    public void freeAdress(Long idAdress) {
        Optional<Adress> adressOptional = adressRepository.findById(idAdress);
        if (adressOptional.isPresent()) {
            Adress adress = adressOptional.get();
            adress.setAdressStatus(AdressStatus.EMPTY);
            adress.setSupplier(null);
            adressRepository.save(adress);
        }
    }


    @Override
    public int getTotalAdresses(String supplierName) {
        return adressRepository.findAdressesBySupplier(supplierRepository.findSupplierBySuppName(supplierName)).size();
    }

    @Override
    public int getAdressesPerLocation(String locationName) {
        return adressRepository.findAdressesByLocation(locationRepository.findLocationByName(locationName)).size();
    }

    @Override
    public List<String> getLocations(String supplierName) {
        List<Location> locations = adressRepository.findAdressesBySupplier(supplierRepository.findSupplierBySuppName(supplierName)).stream().map(Adress::getLocation).toList();
        return locations.stream().map(Location::getName).toList();
    }

    @Override
    public List<String> getCountries(String supplierName) {
        List<Location> locations = adressRepository.findAdressesBySupplier(supplierRepository.findSupplierBySuppName(supplierName)).stream().map(Adress::getLocation).toList();
        return locations.stream().map(Location::getCountry).toList();
    }











}
