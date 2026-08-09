package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.DTO.AddressDTO;
import com.projecttuto.vehicule_rental.DTO.AddressSupplierDTO;
import com.projecttuto.vehicule_rental.entities.Address;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.enums.AddressStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.repositories.AddressRepository;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.AddressService;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository adressRepository;

    private final SupplierRepository supplierRepository;

    private final LocationRepository locationRepository;

    public AddressServiceImpl(AddressRepository adressRepository, SupplierRepository supplierRepository,
                             LocationRepository locationRepository) {
        this.adressRepository = adressRepository;
        this.supplierRepository = supplierRepository;
        this.locationRepository = locationRepository;
    }

    public AddressDTO getAddressDTO(Address adress) {
        AddressDTO adressDTO = new AddressDTO();
        adressDTO.setIdAddress(adress.getIdAddress());
        adressDTO.setRoad(adress.getRoad());
        adressDTO.setNumber(adress.getNumber());
        adressDTO.setAddressStatus(adress.getAddressStatus());
        adressDTO.setSupplierEmail(adress.getSupplier().getEmail());
        adressDTO.setLocation(adress.getLocation().getName());
        return adressDTO;
    }

    public AddressSupplierDTO getAddressSupplierDTO(Address adress) {
        AddressSupplierDTO adressSupplierDTO = new AddressSupplierDTO();
        adressSupplierDTO.setIdAddress(adress.getIdAddress());
        adressSupplierDTO.setRoad(adress.getRoad());
        adressSupplierDTO.setNumber(adress.getNumber());
        adressSupplierDTO.setLocation(adress.getLocation().getName());
        return adressSupplierDTO;
    }


    @Override
    public AddressDTO addAddressToSupplier(AddressDTO adressDTO) {
        Address adress = new Address();
        adress.setRoad(adressDTO.getRoad());
        adress.setNumber(adressDTO.getNumber());
        adress.setAddressStatus(adressDTO.getAddressStatus());
        adress.setSupplier(supplierRepository.findSupplierByEmail(adressDTO.getSupplierEmail()));
        adress.setLocation(locationRepository.findLocationByName(adressDTO.getLocation()));
        Address adressSaved = adressRepository.save(adress);
        return getAddressDTO(adressSaved);
    }

    @Override
    public Page<AddressSupplierDTO> getSuppliersAddresses(int page, int size, String email) {
        Pageable pageable = PageRequest.of(page, size);
        Supplier supplier = supplierRepository.findSupplierByEmail(email);
        return adressRepository.findAddressesBySupplier(supplier,pageable).map(this::getAddressSupplierDTO);
    }

    @Override
    public void freeAddress(Long idAddress) {
        Optional<Address> adressOptional = adressRepository.findById(idAddress);
        if (adressOptional.isPresent()) {
            Address adress = adressOptional.get();
            adress.setAddressStatus(AddressStatus.EMPTY);
            adress.setSupplier(null);
            adressRepository.save(adress);
        }
    }


    @Override
    public int getTotalAddresses(String email) {
        return adressRepository.findAddressesBySupplier(supplierRepository.findSupplierByEmail(email)).size();
    }

    @Override
    public int getAddressesPerLocation(String locationName) {
        return adressRepository.findAddressesByLocation(locationRepository.findLocationByName(locationName)).size();
    }

    @Override
    public List<String> getLocations(String email) {
        List<Location> locations = adressRepository.findAddressesBySupplier(supplierRepository.findSupplierByEmail(email)).stream().map(Address::getLocation).toList();
        return locations.stream().map(Location::getName).toList();
    }

    @Override
    public List<String> getCountries(String email) {
        List<Location> locations = adressRepository.findAddressesBySupplier(supplierRepository.findSupplierByEmail(email)).stream().map(Address::getLocation).toList();
        return locations.stream().map(Location::getCountry).toList();
    }











}
