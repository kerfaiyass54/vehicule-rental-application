package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.*;
import com.projecttuto.vehicule_rental.entities.Adress;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SupplierService {

    SupplierDTO getSupplier(String supplierName);

    public SupplierDetailsDTO getDetails(String email);
    public Integer getSupplierVehicules(String email);
    public Integer getSupplierAdresses(String email);
    public Integer getSupplierCountries(String email);
    public Integer getSupplierLocations(String email);
    public List<AdressDTO> getAdressesList(String email, int size, int page);
    public List<String> getCountries(String email);
    public List<LocationDTO> getLocations(String email, int size, int page);
    Page<SubscriptionResponseDTO> checkSubscriptions(
            String supplierEmail,
            int page,
            int size);
    Page<BuyingResponseDTO> checkBuyings(
            String supplierEmail,
            int page,
            int size);
    Page<DemandResponseDTO> checkDemands(
            String supplierEmail,
            int page,
            int size);

    DemandResponseDTO approveDemand(Long demandId);

    DemandResponseDTO refuseDemand(Long demandId);
    SupplierDashboardDTO getDashboard(String supplierEmail);
    // Vehicule statistics
    public Integer getTotalVehicules(String email);
    public int countBySupplierEmailAndVehiculeStatus(String email, VehiculeStatus status);

    // Vehicule list (card display)
    public List<VehiculeDTO> getVehiculesList(String email);


    public Vehicule addVehiculeNew(VehiculeCreation vehiculeCreation, String supplierEmail);
    public Adress addAdressNew(AddressCreation addressCreation, String supplierEmail);
    public void freeAddress(Long AddressId);
    public List<String> getVehiculesNames(String email);
    public List<Long> getVehiculesIds(String email);

}
