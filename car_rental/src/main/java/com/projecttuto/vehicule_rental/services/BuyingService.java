package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.BuyingDTO;
import com.projecttuto.vehicule_rental.entities.Buying;
import org.springframework.data.domain.Page;

public interface BuyingService {

    Buying addBuying(
            Long vehiculeId,
            String clientEmail,
            Integer period,
            boolean renew
    );

    Page<BuyingDTO> getBuyingByClient(
            String clientEmail,
            int page,
            int size
    );
}