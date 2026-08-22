package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.VehiculeCreation;
import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeListDTO;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import com.projecttuto.vehicule_rental.exception.VehiculeRentalException;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.SupplierVehiculesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierVehiculesServiceImpl implements SupplierVehiculesService {

    private final SupplierRepository supplierRepository;
    private final VehiculeRepository vehiculeRepository;


    // =========================================================
    // SUPPLIER VEHICLES COUNT
    // =========================================================

    @Override
    public Integer getSupplierVehicules(String email) {

        Supplier supplier = findSupplierByEmail(email);

        return supplier.getVehicles().size();
    }


    @Override
    public Integer getTotalVehicules(String email) {

        Supplier supplier = findSupplierByEmail(email);

        if (supplier.getVehicles() == null) {
            return 0;
        }

        return supplier.getVehicles().size();
    }


    @Override
    public int countBySupplierEmailAndVehiculeStatus(
            String email,
            VehiculeStatus status) {

        Supplier supplier = findSupplierByEmail(email);

        if (supplier.getVehicles() == null) {
            return 0;
        }

        return (int) supplier.getVehicles()
                .stream()
                .filter(vehicle -> vehicle.getVehicleStatus() == status)
                .count();
    }


    // =========================================================
    // VEHICLE LIST
    // =========================================================

    public VehiculeListDTO getVehiculeList(Vehicule vehicule) {

        VehiculeListDTO dto = new VehiculeListDTO();

        dto.setIdVehicule(vehicule.getIdVehicle());
        dto.setTransmission(vehicule.getTransmission());
        dto.setPrice(vehicule.getPrice());
        dto.setNameVehicule(vehicule.getVehicleName());

        return dto;
    }


    @Override
    public Page<VehiculeListDTO> getVehiculeList(
            int size,
            int page,
            String supplierName) {

        Pageable pageable = PageRequest.of(page, size);

        Supplier supplier = findSupplierByEmail(supplierName);

        return vehiculeRepository
                .findVehiculesBySupplier(supplier, pageable)
                .map(this::getVehiculeList);
    }


    @Override
    public List<VehiculeDTO> getVehiculesList(String email) {

        Supplier supplier = findSupplierByEmail(email);

        if (supplier.getVehicles() == null) {
            return List.of();
        }

        return supplier.getVehicles()
                .stream()
                .map(this::toVehiculeDTO)
                .toList();
    }


    private VehiculeDTO toVehiculeDTO(Vehicule vehicule) {

        VehiculeDTO dto = new VehiculeDTO();

        dto.setIdVehicule(vehicule.getIdVehicle());
        dto.setNameVehicule(vehicule.getVehicleName());
        dto.setBrand(vehicule.getBrand());
        dto.setColor(vehicule.getColor());
        dto.setPrice(vehicule.getPrice());
        dto.setHighSpeed(vehicule.getMaxSpeed());
        dto.setTransmission(vehicule.getTransmission());
        dto.setVehiculeStatus(vehicule.getVehicleStatus());

        return dto;
    }


    // =========================================================
    // CREATE VEHICLE
    // =========================================================

    @Override
    public Vehicule addVehiculeNew(
            VehiculeCreation vehiculeCreation,
            String supplierEmail) {

        Supplier supplier = findSupplierByEmail(supplierEmail);

        Vehicule vehicule = buildVehicule(
                vehiculeCreation,
                supplier
        );

        return vehiculeRepository.save(vehicule);
    }


    private Vehicule buildVehicule(
            VehiculeCreation vehiculeCreation,
            Supplier supplier) {

        Vehicule vehicule = new Vehicule();

        vehicule.setVehicleName(
                vehiculeCreation.getNameVehicule());

        vehicule.setBrand(
                vehiculeCreation.getBrand());

        vehicule.setColor(
                vehiculeCreation.getColor());

        vehicule.setPrice(
                vehiculeCreation.getPrice());

        vehicule.setMaxSpeed(
                vehiculeCreation.getHighSpeed());

        vehicule.setTransmission(
                vehiculeCreation.getTransmission());

        vehicule.setVehicleStatus(
                VehiculeStatus.AVAILABLE);

        vehicule.setSupplier(supplier);

        return vehicule;
    }


    // =========================================================
    // VEHICLE NAMES
    // =========================================================

    @Override
    public List<String> getVehiculesNames(String email) {

        Supplier supplier = findSupplierByEmail(email);

        if (supplier.getVehicles() == null) {
            return List.of();
        }

        return supplier.getVehicles()
                .stream()
                .map(Vehicule::getVehicleName)
                .toList();
    }


    // =========================================================
    // VEHICLE IDS
    // =========================================================

    @Override
    public List<Long> getVehiculesIds(String email) {

        Supplier supplier = findSupplierByEmail(email);

        if (supplier.getVehicles() == null) {
            return List.of();
        }

        return supplier.getVehicles()
                .stream()
                .map(Vehicule::getIdVehicle)
                .toList();
    }


    // =========================================================
    // SUPPLIER LOOKUPS
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


    private Supplier findSupplierByName(String supplierName) {

        Supplier supplier =
                supplierRepository.findSupplierBySupplierName(supplierName);

        if (supplier == null) {
            throw new VehiculeRentalException(
                    "Supplier not found"
            );
        }

        return supplier;
    }
}