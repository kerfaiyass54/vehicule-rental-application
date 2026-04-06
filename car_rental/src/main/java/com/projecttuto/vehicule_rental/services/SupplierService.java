package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.SupplierDTO;
import com.projecttuto.vehicule_rental.DTO.SupplierDetailsDTO;
import com.projecttuto.vehicule_rental.entities.Adress;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;

import java.util.List;

public interface SupplierService {
    void addSupplier(Supplier supplier);
    void updateSupplier(SupplierDTO supplierDTO);
    void deleteSupplier(String name);
    SupplierDTO getSupplier(String supplierName);
    void changeSupplierPassword(Supplier supplier, String newPassword);
    List<Subscription> getSubscriptions(Supplier supplier);
    List<Adress> getAdresses(Supplier supplier);
    List<Vehicule> getVehicules(Supplier supplier);
    public SupplierDetailsDTO getDetails(String email);
    public Integer getSupplierVehicules(String email);
    public Integer getSupplierCategories(String email);
    public Integer getSupplierAdresses(String email);

}
