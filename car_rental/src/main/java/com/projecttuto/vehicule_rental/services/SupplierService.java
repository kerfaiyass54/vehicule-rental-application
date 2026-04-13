package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.*;
import com.projecttuto.vehicule_rental.entities.Adress;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;

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
    public Integer getSupplierCountries(String email);
    public Integer getSupplierLocations(String email);
    public List<AdressDTO> getAdressesList(String email, int size, int page);
    public List<String> getCountries(String email);
    public List<LocationDTO> getLocations(String email, int size, int page);
    // Vehicule statistics
    public Integer getTotalVehicules(String email);
    public int countBySupplierEmailAndVehiculeStatus(String email, VehiculeStatus status);

    // Vehicule list (card display)
    public List<VehiculeDTO> getVehiculesList(String email);

    // Vehicule management
    public void updateVehicule(VehiculeUpdate vehiculeUpdate);
    public void addVehicule(VehiculeDTO vehiculeDTO);
    public Integer getTotalCategories(String email);
    public Integer getTotalStock(String email);
    public List<CategoryDTO> getCategoryList(String email);
    public Integer getStockContent(String email, String nameCategory);
    public CategoryDTO addCategory(CategoryDTO categoryDTO, String supplierEmail);

}
