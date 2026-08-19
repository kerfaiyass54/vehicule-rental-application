package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.SubscriptionResponseDTO;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.repositories.SubscriptionRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.SupplierSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierSubscriptionServiceImpl
        implements SupplierSubscriptionService {

    private final SupplierRepository supplierRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Page<SubscriptionResponseDTO> checkSubscriptions(
            String supplierEmail,
            int page,
            int size) {

        Supplier supplier = findSupplierByEmail(supplierEmail);

        Pageable pageable = PageRequest.of(page, size);

        return subscriptionRepository
                .findBySupplier(supplier, pageable)
                .map(this::mapToDTO);
    }

    private Supplier findSupplierByEmail(String supplierEmail) {

        Supplier supplier =
                supplierRepository.findSupplierByEmail(supplierEmail);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        return supplier;
    }

    private SubscriptionResponseDTO mapToDTO(
            com.projecttuto.vehicule_rental.entities.Subscription subscription) {

        SubscriptionResponseDTO dto =
                new SubscriptionResponseDTO();

        dto.setIdSubscription(
                subscription.getIdSubscription());

        dto.setClientName(
                subscription.getClient().getClientName());

        dto.setClientEmail(
                subscription.getClient().getEmail());

        dto.setType(
                subscription.getSubscriptionType());

        dto.setDateStart(
                subscription.getDateStart());

        dto.setPrice(
                subscription.getPrice());

        dto.setReduce(
                subscription.getReduction());

        return dto;
    }
}