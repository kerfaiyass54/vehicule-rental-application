package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.dto.SupplierInfoDTO;
import com.projecttuto.vehicule_rental.enums.SubscriptionType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubscriptionService {


    SubscripionInfoDTO addSubscription(SubscripionInfoDTO dto);

    SubscripionInfoDTO renewSubscription(String clientEmail);

    void cancelSubscription(String clientEmail);

    public boolean isSubscribed(Long supplierId, Long clientId);

    List<SupplierInfoDTO> getSubscribedSuppliers(Long clientId);

    List<SupplierInfoDTO> getUnsubscribedSuppliers(Long clientId);

    Double getReduction(SubscriptionType  subscriptionType);

    Page<SubscripionInfoDTO> getSubscription(String clientEmail, int size, int page);





}
