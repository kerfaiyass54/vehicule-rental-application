package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.BuyingResponseDTO;
import org.springframework.data.domain.Page;

public interface SupplierBuyingService {
    Page<BuyingResponseDTO> checkBuyings(
            String supplierEmail,
            int page,
            int size);
}
