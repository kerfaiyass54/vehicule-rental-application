package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.SupplierDashboardDTO;
import com.projecttuto.vehicule_rental.dto.SupplierDetailsDTO;

public interface SupplierDetailsService {
    public SupplierDetailsDTO getDetails(String email);
    SupplierDashboardDTO getDashboard(String supplierEmail);



}
