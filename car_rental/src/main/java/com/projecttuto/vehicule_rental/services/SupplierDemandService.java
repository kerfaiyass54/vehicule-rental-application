package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.DemandResponseDTO;
import org.springframework.data.domain.Page;

public interface SupplierDemandService {



    Page<DemandResponseDTO> checkDemands(
            String supplierEmail,
            int page,
            int size);


    DemandResponseDTO approveDemand(Long demandId);

    DemandResponseDTO refuseDemand(Long demandId);
}
