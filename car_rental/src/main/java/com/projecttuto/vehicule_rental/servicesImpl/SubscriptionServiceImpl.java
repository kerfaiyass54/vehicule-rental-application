package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.dto.SupplierInfoDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.enums.SubscriptionType;
import com.projecttuto.vehicule_rental.exception.VehiculeRentalException;
import com.projecttuto.vehicule_rental.mappers.SupplierMapper;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.SubscriptionRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ClientRepository clientRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public Double getReduction(SubscriptionType subscriptionType){
        return subscriptionType.getReduction();
    }

    @Override
    public List<SupplierInfoDTO> getUnsubscribedSuppliers(Long clientId) {

        return supplierRepository.findAll()
                .stream()
                .filter(supplier ->
                        !isSubscribed(
                                supplier.getIdSupplier(),
                                clientId
                        )
                )
                .map(supplierMapper::toInfoDTO)
                .toList();
    }

    @Override
    public Page<SubscripionInfoDTO> getSubscription(
            String clientEmail,
            int size,
            int page) {

        // ---------------------------------------------------------
        // FIND CLIENT
        // ---------------------------------------------------------

        Client client = findClientByEmail(clientEmail);

        // ---------------------------------------------------------
        // PAGINATION
        // ---------------------------------------------------------

        Pageable pageable =
                PageRequest.of(page, size);

        // ---------------------------------------------------------
        // FETCH SUBSCRIPTIONS
        // ---------------------------------------------------------

        return subscriptionRepository
                .findByClient(client, pageable)
                .map(this::mapToDTO);
    }




    @Override
    public List<SupplierInfoDTO> getSubscribedSuppliers(Long clientId) {

        return supplierRepository
                .findSubscribedSuppliers(clientId)
                .stream()
                .map(supplierMapper::toInfoDTO)
                .toList();
    }


    @Override
    public boolean isSubscribed(Long supplierId, Long clientId) {

        return subscriptionRepository
                .existsBySupplier_IdSupplierAndClient_IdClient(
                        supplierId,
                        clientId
                );
    }


    @Override
    public void cancelSubscription(String clientEmail) {

        Client client = findClientByEmail(clientEmail);

        Subscription subscription =
                findSubscriptionByClient(client);

        subscriptionRepository.delete(subscription);
    }


    @Override
    public SubscripionInfoDTO renewSubscription(
            String clientEmail) {

        Client client = findClientByEmail(clientEmail);

        Subscription subscription =
                findSubscriptionByClient(client);

        updateSubscriptionDate(subscription);

        Subscription updated =
                subscriptionRepository.save(subscription);

        return mapToDTO(updated);
    }


    @Override
    public SubscripionInfoDTO addSubscription(
            SubscripionInfoDTO dto) {

        Client client =
                findClientByEmail(dto.getClientEmail());

        Supplier supplier =
                findSupplierByName(dto.getSupplierName());

        validateNoExistingSubscription(client);

        Subscription subscription =
                createSubscription(dto, client, supplier);

        Subscription saved =
                subscriptionRepository.save(subscription);

        return mapToDTO(saved);
    }


    // =========================================================
    // FIND METHODS
    // =========================================================

    private Client findClientByEmail(String clientEmail) {

        Client client =
                clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new VehiculeRentalException(
                    "Client not found"
            );
        }

        return client;
    }


    private Supplier findSupplierByName(String supplierName) {

        Supplier supplier =
                supplierRepository.findSupplierBySupplierName(
                        supplierName
                );

        if (supplier == null) {
            throw new VehiculeRentalException(
                    "Supplier not found"
            );
        }

        return supplier;
    }


    private Subscription findSubscriptionByClient(
            Client client) {

        return subscriptionRepository
                .findByClient(client)
                .orElseThrow(() ->
                        new VehiculeRentalException(
                                "Subscription not found"
                        )
                );
    }


    // =========================================================
    // VALIDATION
    // =========================================================

    private void validateNoExistingSubscription(
            Client client) {

        if (subscriptionRepository
                .findByClient(client)
                .isPresent()) {

            throw new VehiculeRentalException(
                    "Client already has a subscription."
            );
        }
    }


    // =========================================================
    // SUBSCRIPTION CREATION
    // =========================================================

    private Subscription createSubscription(
            SubscripionInfoDTO dto,
            Client client,
            Supplier supplier) {

        Subscription subscription = new Subscription();

        subscription.setSubscriptionType(dto.getType());
        subscription.setDateStart(Instant.now());
        subscription.setSupplier(supplier);
        subscription.setClient(client);

        setSubscriptionPricing(subscription);

        return subscription;
    }


    private void setSubscriptionPricing(
            Subscription subscription) {

        switch (subscription.getSubscriptionType()) {

            case BASIC:
                subscription.setPrice(100.0);
                subscription.setReduction(5);
                break;

            case PREMIUM:
                subscription.setPrice(250.0);
                subscription.setReduction(20);
                break;

            case MONTHLY:
                subscription.setPrice(40.0);
                subscription.setReduction(10);
                break;

            case ANNUAL:
                subscription.setPrice(400.0);
                subscription.setReduction(30);
                break;
        }
    }


    // =========================================================
    // UPDATE
    // =========================================================

    private void updateSubscriptionDate(
            Subscription subscription) {

        subscription.setDateStart(Instant.now());
    }


    // =========================================================
    // DTO MAPPING
    // =========================================================

    private SubscripionInfoDTO mapToDTO(
            Subscription subscription) {

        SubscripionInfoDTO dto =
                new SubscripionInfoDTO();

        dto.setIdSubscrip(
                subscription.getIdSubscription()
        );

        dto.setType(
                subscription.getSubscriptionType()
        );

        dto.setDateStart(
                subscription.getDateStart()
        );

        dto.setPrice(
                subscription.getPrice()
        );

        dto.setReduce(
                subscription.getReduction()
        );

        dto.setSupplierName(
                subscription.getSupplier().getSupplierName()
        );

        dto.setClientEmail(
                subscription.getClient().getEmail()
        );

        return dto;
    }
}