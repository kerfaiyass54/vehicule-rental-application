package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.AddressDTO;
import com.projecttuto.vehicule_rental.dto.AddressSupplierDTO;
import com.projecttuto.vehicule_rental.entities.Address;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.enums.AddressStatus;
import com.projecttuto.vehicule_rental.exceptions.VehiculeRentalException;
import com.projecttuto.vehicule_rental.repositories.AddressRepository;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.SupplierAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierAddressServiceImpl implements SupplierAddressService {

    private final AddressRepository addressRepository;
    private final LocationRepository locationRepository;
    private final SupplierRepository supplierRepository;


    @Override
    public Integer getSupplierAddresses(String email) {

        Supplier supplier = findSupplierByEmail(email);

        return supplier.getAddresses().size();
    }


    @Override
    public AddressDTO addAddressToSupplier(
            AddressDTO addressDTO) {

        Supplier supplier =
                findSupplierByEmail(
                        addressDTO.getSupplierEmail()
                );

        Location location =
                findLocationByName(
                        addressDTO.getLocation()
                );

        Address address =
                createAddress(addressDTO, supplier, location);

        Address savedAddress =
                addressRepository.save(address);

        return getAddressDTO(savedAddress);
    }


    @Override
    public Page<AddressSupplierDTO> getSuppliersAddresses(
            int page,
            int size,
            String email) {

        Supplier supplier = findSupplierByEmail(email);

        Pageable pageable =
                PageRequest.of(page, size);

        return addressRepository
                .findAddressesBySupplier(
                        supplier,
                        pageable
                )
                .map(this::getAddressSupplierDTO);
    }


    @Override
    public void freeAddress(Long idAddress) {

        Address address = findAddressById(idAddress);

        freeAddressFromSupplier(address);

        addressRepository.save(address);
    }


    // =========================================================
    // FIND METHODS
    // =========================================================

    private Supplier findSupplierByEmail(String email) {

        Supplier supplier =
                supplierRepository.findSupplierByEmail(email);

        if (supplier == null) {
            throw new VehiculeRentalException(
                    "Supplier not found"
            );
        }

        return supplier;
    }


    private Location findLocationByName(String locationName) {

        Location location =
                locationRepository.findLocationByName(
                        locationName
                );

        if (location == null) {
            throw new VehiculeRentalException(
                    "Location not found"
            );
        }

        return location;
    }


    private Address findAddressById(Long idAddress) {

        return addressRepository.findById(idAddress)
                .orElseThrow(() ->
                        new VehiculeRentalException(
                                "Address not found"
                        )
                );
    }


    // =========================================================
    // ADDRESS CREATION
    // =========================================================

    private Address createAddress(
            AddressDTO dto,
            Supplier supplier,
            Location location) {

        Address address = new Address();

        address.setRoad(dto.getRoad());
        address.setNumber(dto.getNumber());
        address.setAddressStatus(
                dto.getAddressStatus()
        );
        address.setSupplier(supplier);
        address.setLocation(location);

        return address;
    }


    // =========================================================
    // ADDRESS OPERATIONS
    // =========================================================

    private void freeAddressFromSupplier(
            Address address) {

        address.setAddressStatus(
                AddressStatus.EMPTY
        );

        address.setSupplier(null);
    }


    // =========================================================
    // DTO MAPPING
    // =========================================================

    public AddressDTO getAddressDTO(
            Address address) {

        AddressDTO dto = new AddressDTO();

        dto.setIdAddress(
                address.getIdAddress()
        );

        dto.setRoad(
                address.getRoad()
        );

        dto.setNumber(
                address.getNumber()
        );

        dto.setAddressStatus(
                address.getAddressStatus()
        );

        dto.setSupplierEmail(
                address.getSupplier().getEmail()
        );

        dto.setLocation(
                address.getLocation().getLocationName()
        );

        return dto;
    }


    public AddressSupplierDTO getAddressSupplierDTO(
            Address address) {

        AddressSupplierDTO dto =
                new AddressSupplierDTO();

        dto.setIdAddress(
                address.getIdAddress()
        );

        dto.setRoad(
                address.getRoad()
        );

        dto.setNumber(
                address.getNumber()
        );

        dto.setLocation(
                address.getLocation().getLocationName()
        );

        return dto;
    }
}