package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.AdressDTO;
import com.projecttuto.vehicule_rental.DTO.AdressSupplierDTO;
import org.springframework.data.domain.Page;

import java.util.List;


public interface AdressService {

    public AdressDTO addAddressToSupplier(AdressDTO adressDTO);
    public Page<AdressSupplierDTO> getSuppliersAdresses(int page, int size, String email);
    public void freeAdress(Long idAdress);
    public int getTotalAdresses(String email);
    public int getAdressesPerLocation(String locationName);
    public List<String> getLocations(String email);
    public List<String> getCountries(String email);

}
