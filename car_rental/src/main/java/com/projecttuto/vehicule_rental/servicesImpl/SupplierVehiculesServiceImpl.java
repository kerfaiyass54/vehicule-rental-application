package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.VehiculeCreation;
import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeListDTO;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
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

    @Override
    public Integer getSupplierVehicules(String email) {

        return supplierRepository.findSupplierByEmail(email)
                .getVehicles()
                .size();
    }

    public VehiculeListDTO getVehiculeList(Vehicule vehicule){
        VehiculeListDTO vehiculeListDTO = new VehiculeListDTO();
        vehiculeListDTO.setIdVehicule(vehicule.getIdVehicle());
        vehiculeListDTO.setTransmission(vehicule.getTransmission());
        vehiculeListDTO.setPrice(vehicule.getPrice());
        vehiculeListDTO.setNameVehicule(vehicule.getVehicleName());
        return vehiculeListDTO;
    }

    @Override
    public Page<VehiculeListDTO> getVehiculeList(int size, int page, String supplierName){
        Pageable pageable = PageRequest.of(page, size);
        Supplier supplier = supplierRepository.findSupplierBySuppName(supplierName);
        return vehiculeRepository.findVehiculesBySupplier(supplier, pageable).map(this::getVehiculeList);
    }


    @Override
    public Integer getTotalVehicules(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getVehicles() == null)
            return 0;

        return supplier.getVehicles().size();
    }


    @Override
    public int countBySupplierEmailAndVehiculeStatus(String email, VehiculeStatus status) {

        return (int) supplierRepository.findSupplierByEmail(email)
                .getVehicles()
                .stream()
                .filter(v -> v.getVehicleStatus() == status)
                .count();
    }


    @Override
    public List<VehiculeDTO> getVehiculesList(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getVehicles() == null)
            return List.of();

        return supplier.getVehicles()
                .stream()
                .map(vehicule -> {

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

                }).toList();
    }






    @Override
    public Vehicule addVehiculeNew(VehiculeCreation vehiculeCreation, String supplierEmail){
        Vehicule vehicule = new Vehicule();
        vehicule.setVehicleName(vehiculeCreation.getNameVehicule());
        vehicule.setBrand(vehiculeCreation.getBrand());
        vehicule.setColor(vehiculeCreation.getColor());
        vehicule.setPrice(vehiculeCreation.getPrice());
        vehicule.setMaxSpeed(vehiculeCreation.getHighSpeed());
        vehicule.setTransmission(vehiculeCreation.getTransmission());
        vehicule.setVehicleStatus(VehiculeStatus.AVAILABLE);
        vehicule.setSupplier(supplierRepository.findSupplierByEmail(supplierEmail));
        return vehiculeRepository.save(vehicule);
    }



    @Override
    public List<String> getVehiculesNames(String email){
        Supplier supplier = supplierRepository.findSupplierByEmail(email);
        return supplier.getVehicles().stream().map(Vehicule::getVehicleName).toList();
    }




    @Override
    public List<Long> getVehiculesIds(String email){
        Supplier supplier = supplierRepository.findSupplierByEmail(email);
        return supplier.getVehicles().stream().map(Vehicule::getIdVehicle).toList();
    }
}
