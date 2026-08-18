package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.AddressDTO;
import com.projecttuto.vehicule_rental.dto.AddressSupplierDTO;
import org.springframework.data.domain.Page;

public interface SupplierAddressService {

    public AddressDTO addAddressToSupplier(AddressDTO adressDTO);
    public Page<AddressSupplierDTO> getSuppliersAddresses(int page, int size, String email);
    public void freeAddress(Long idAddress);
    public int getTotalAddresses(String email);

}
