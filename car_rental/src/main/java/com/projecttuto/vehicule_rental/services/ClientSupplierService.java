package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.SupplierInfoDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeSupplierDTO;
import org.springframework.data.domain.Page;

public interface ClientSupplierService {

    Page<SupplierInfoDTO> searchSuppliers(int page, int size);

    Page<SupplierInfoDTO> searchSuppliers(
            String keyword,
            int page,
            int size
    );


    Page<VehiculeSupplierDTO> getAvailableVehiculesBySupplier(
            Long supplierId,
            int page,
            int size
    );
}
