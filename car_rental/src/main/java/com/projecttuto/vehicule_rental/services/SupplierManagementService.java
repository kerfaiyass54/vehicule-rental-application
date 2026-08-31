package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.SupplierAdminDTO;
import org.springframework.data.domain.Page;

public interface SupplierManagementService {
    Page<SupplierAdminDTO> getSuppliers(int page, int size);

    SupplierAdminDTO getSupplier(Long id);


    SupplierAdminDTO updateSupplier(Long id, SupplierAdminDTO dto);

    void deleteSupplier(Long id);

    SupplierAdminDTO createSupplier(SupplierAdminDTO dto);
}
