package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.SubscripionInfoDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.exception.VehiculeRentalException;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.SubscriptionRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ClientRepository clientRepository;
    private final SupplierRepository supplierRepository;


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
                supplierRepository.findSupplierBySuppName(
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