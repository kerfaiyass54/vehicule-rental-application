package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.DTO.SupplierDTO;
import com.projecttuto.vehicule_rental.entities.*;
import com.projecttuto.vehicule_rental.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.services.SupplierService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private AdressRepository adressRepository;

    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private AdminRepository adminRepository;


    @Override
    public void addSupplier(Supplier supplier){
        supplierRepository.save(supplier);
    }

    @Override
    public void updateSupplier(SupplierDTO supplierDTO){
        Supplier supplier1 = supplierRepository.findById(supplierDTO.getIdSupp()).get();
        supplier1.setNationality(supplierDTO.getNationality());
        supplier1.setPass(supplierDTO.getPass());
        supplier1.setSuppName(supplierDTO.getSuppName());
        supplier1.setEmail(supplierDTO.getEmail());
        supplierRepository.save(supplier1);
    }

    @Override
    public void deleteSupplier(String name){
        supplierRepository.delete(supplierRepository.findBySuppName(name).get());
    }

    @Override
    public SupplierDTO getSupplier(String supplierName){
        SupplierDTO supplierDTO = new SupplierDTO();
        Optional<Supplier> supplier = supplierRepository.findBySuppName(supplierName);
        Supplier supplier1 = supplier.get();
        supplierDTO.setIdSupp(supplier1.getIdSupp());
        supplierDTO.setNationality(supplier1.getNationality());
        supplierDTO.setSuppName(supplier1.getSuppName());
        supplierDTO.setEmail(supplier1.getEmail());
        return supplierDTO;
    }

    @Override
    public void changeSupplierPassword(Supplier supplier, String newPassword){

    }

    @Override
    public List<Subscription> getSubscriptions(Supplier supplier){
        return supplierRepository.getById(supplier.getIdSupp()).getSubscriptions();
    }
    @Override
    public List<Adress> getAdresses(Supplier supplier){
        return supplierRepository.getById(supplier.getIdSupp()).getAdresses();
    }
    @Override
    public List<Vehicule> getVehicules(Supplier supplier){
        return supplierRepository.getById(supplier.getIdSupp()).getVehicules();
    }

}
