package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.SubscriptionResponseDTO;
import org.springframework.data.domain.Page;

public interface SupplierSubscriptionService {
    Page<SubscriptionResponseDTO> checkSubscriptions(
            String supplierEmail,
            int page,
            int size);

}
