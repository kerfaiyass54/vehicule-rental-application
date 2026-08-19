package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeUpdate;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.VehiculeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final SupplierRepository supplierRepository;


    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository, SupplierRepository supplierRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.supplierRepository = supplierRepository;
    }

    public VehiculeDTO getVehicule(Vehicule vehicule){
        VehiculeDTO vehiculeDTO = new VehiculeDTO();
        vehiculeDTO.setIdVehicule(vehicule.getIdVehicle());
        vehiculeDTO.setNameVehicule(vehicule.getVehicleName());
        vehiculeDTO.setSupplier(vehicule.getSupplier().getEmail());
        vehiculeDTO.setBrand(vehicule.getBrand());
        vehiculeDTO.setPrice(vehicule.getPrice());
        vehiculeDTO.setHighSpeed(vehicule.getMaxSpeed());
        vehiculeDTO.setTransmission(vehicule.getTransmission());
        vehiculeDTO.setVehiculeStatus(vehicule.getVehicleStatus());
        return vehiculeDTO;
    }


  

    @Override
    public VehiculeDTO addVehicule(VehiculeDTO vehiculeDTO){
        Vehicule vehicule = new Vehicule();
        vehicule.setVehicleName(vehiculeDTO.getNameVehicule());
        vehicule.setColor(vehiculeDTO.getColor());
        vehicule.setBrand(vehiculeDTO.getBrand());
        vehicule.setPrice(vehiculeDTO.getPrice());
        vehicule.setMaxSpeed(vehiculeDTO.getHighSpeed());
        vehicule.setTransmission(vehiculeDTO.getTransmission());
        vehicule.setVehicleStatus(vehiculeDTO.getVehiculeStatus());
        vehicule.setSupplier(supplierRepository.findSupplierByEmail(vehiculeDTO.getSupplier()));
        vehiculeRepository.save(vehicule);
        return getVehicule(vehicule);
    }

    

    @Override
    public VehiculeDTO getVehiculeById(Long id){
        Optional<Vehicule> vehicule =  vehiculeRepository.findById(id);
        return vehicule.map(this::getVehicule).orElse(null);
    }

    @Override
    public void updateVehicule(VehiculeUpdate vehiculeUpdate, Long id){
        Optional<Vehicule> vehicule =  vehiculeRepository.findById(id);
        if(vehicule.isPresent()){
            Vehicule vehiculeUpdated = vehicule.get();
            vehiculeUpdated.setColor(vehiculeUpdate.getColor());
            vehiculeUpdated.setMaxSpeed(vehiculeUpdate.getHighSpeed());
            vehiculeUpdated.setPrice(vehiculeUpdate.getPrice());
        }
    }

    


}
