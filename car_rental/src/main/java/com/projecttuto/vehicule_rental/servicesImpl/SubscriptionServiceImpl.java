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
    public SubscripionInfoDTO getSubscriptionDetails(
            String clientEmail,
            String supplierEmail) {

        // ---------------------------------------------------------
        // VALIDATE CLIENT
        // ---------------------------------------------------------

        if (clientEmail == null || clientEmail.isBlank()) {
            throw new VehiculeRentalException(
                    "Client email is required"
            );
        }

        // ---------------------------------------------------------
        // VALIDATE SUPPLIER
        // ---------------------------------------------------------

        if (supplierEmail == null || supplierEmail.isBlank()) {
            throw new VehiculeRentalException(
                    "Supplier email is required"
            );
        }

        // ---------------------------------------------------------
        // FIND CLIENT
        // ---------------------------------------------------------

        Client client =
                findClientByEmail(clientEmail);

        // ---------------------------------------------------------
        // FIND SUPPLIER
        // ---------------------------------------------------------

        Supplier supplier =
                supplierRepository.findSupplierByEmail(
                        supplierEmail
                );

        if (supplier == null) {
            throw new VehiculeRentalException(
                    "Supplier not found"
            );
        }

        // ---------------------------------------------------------
        // FIND SUBSCRIPTION
        // ---------------------------------------------------------

        Subscription subscription =
                subscriptionRepository
                        .findByClient_EmailAndSupplier_Email(
                                client.getEmail(),
                                supplier.getEmail()
                        );

        log.info("Subscription found for client={}, supplier={}", client, supplier);

        // ---------------------------------------------------------
        // RETURN DTO
        // ---------------------------------------------------------

        return mapToDTO(subscription);
    }


    // =========================================================
    // REDUCTION
    // =========================================================

    @Override
    public Double getReduction(
            SubscriptionType subscriptionType) {

        return 100-subscriptionType.getReduction();
    }


    // =========================================================
    // GET UNSUBSCRIBED SUPPLIERS
    // =========================================================

    @Override
    public List<SupplierInfoDTO> getUnsubscribedSuppliers(
            String clientEmail) {

        // ---------------------------------------------------------
        // FIND CLIENT
        // ---------------------------------------------------------

        Client client =
                findClientByEmail(clientEmail);

        Long clientId =
                client.getIdClient();

        // ---------------------------------------------------------
        // FIND UNSUBSCRIBED SUPPLIERS
        // ---------------------------------------------------------

        return supplierRepository.findAll()
                .stream()
                .filter(supplier ->
                        !subscriptionRepository
                                .existsBySupplier_IdSupplierAndClient_IdClient(
                                        supplier.getIdSupplier(),
                                        clientId
                                )
                )
                .map(supplierMapper::toInfoDTO)
                .toList();
    }


    // =========================================================
    // GET SUBSCRIPTIONS
    // =========================================================

    @Override
    public Page<SubscripionInfoDTO> getSubscription(
            String clientEmail,
            int size,
            int page) {

        // ---------------------------------------------------------
        // FIND CLIENT
        // ---------------------------------------------------------

        Client client =
                findClientByEmail(clientEmail);

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


    // =========================================================
    // GET SUBSCRIBED SUPPLIERS
    // =========================================================

    @Override
    public List<SupplierInfoDTO> getSubscribedSuppliers(
            String clientEmail) {

        // ---------------------------------------------------------
        // FIND CLIENT
        // ---------------------------------------------------------

        Client client =
                findClientByEmail(clientEmail);

        Long clientId =
                client.getIdClient();

        // ---------------------------------------------------------
        // FIND SUBSCRIBED SUPPLIERS
        // ---------------------------------------------------------

        return supplierRepository
                .findSubscribedSuppliers(clientId)
                .stream()
                .map(supplierMapper::toInfoDTO)
                .toList();
    }


    // =========================================================
    // CHECK SUBSCRIPTION
    // =========================================================

    @Override
    public boolean isSubscribed(
            Long supplierId,
            String clientEmail) {

        // ---------------------------------------------------------
        // FIND CLIENT
        // ---------------------------------------------------------

        Client client =
                findClientByEmail(clientEmail);

        Long clientId =
                client.getIdClient();

        // ---------------------------------------------------------
        // CHECK EXISTING SUBSCRIPTION
        // ---------------------------------------------------------

        return subscriptionRepository
                .existsBySupplier_IdSupplierAndClient_IdClient(
                        supplierId,
                        clientId
                );
    }


    // =========================================================
    // CANCEL SUBSCRIPTION
    // =========================================================

    @Override
    public void cancelSubscription(
            String clientEmail) {

        // ---------------------------------------------------------
        // FIND CLIENT
        // ---------------------------------------------------------

        Client client =
                findClientByEmail(clientEmail);

        // ---------------------------------------------------------
        // FIND SUBSCRIPTION
        // ---------------------------------------------------------

        Subscription subscription =
                findSubscriptionByClient(client);

        // ---------------------------------------------------------
        // DELETE SUBSCRIPTION
        // ---------------------------------------------------------

        subscriptionRepository.delete(subscription);
    }


    // =========================================================
    // RENEW SUBSCRIPTION
    // =========================================================

    @Override
    public SubscripionInfoDTO renewSubscription(
            String clientEmail) {

        // ---------------------------------------------------------
        // FIND CLIENT
        // ---------------------------------------------------------

        Client client =
                findClientByEmail(clientEmail);

        // ---------------------------------------------------------
        // FIND SUBSCRIPTION
        // ---------------------------------------------------------

        Subscription subscription =
                findSubscriptionByClient(client);

        // ---------------------------------------------------------
        // UPDATE DATE
        // ---------------------------------------------------------

        updateSubscriptionDate(subscription);

        // ---------------------------------------------------------
        // SAVE
        // ---------------------------------------------------------

        Subscription updated =
                subscriptionRepository.save(subscription);

        // ---------------------------------------------------------
        // RETURN DTO
        // ---------------------------------------------------------

        return mapToDTO(updated);
    }


    // =========================================================
    // ADD SUBSCRIPTION
    // =========================================================

    @Override
    public SubscripionInfoDTO addSubscription(
            SubscripionInfoDTO dto) {

        // ---------------------------------------------------------
        // FIND CLIENT
        // ---------------------------------------------------------

        Client client =
                findClientByEmail(dto.getClientEmail());

        // ---------------------------------------------------------
        // FIND SUPPLIER
        // ---------------------------------------------------------

        Supplier supplier =
                supplierRepository.findByIdSupplier(dto.getIdSupplier());

        Subscription subscription =
                createSubscription(
                        dto,
                        client,
                        supplier
                );

        // ---------------------------------------------------------
        // SAVE
        // ---------------------------------------------------------

        Subscription saved =
                subscriptionRepository.save(subscription);

        // ---------------------------------------------------------
        // RETURN DTO
        // ---------------------------------------------------------

        return mapToDTO(saved);
    }


    // =========================================================
    // FIND CLIENT
    // =========================================================

    private Client findClientByEmail(
            String clientEmail) {

        Client client =
                clientRepository.findClientByEmail(
                        clientEmail
                );

        if (client == null) {

            throw new VehiculeRentalException(
                    "Client not found"
            );
        }

        return client;
    }


    // =========================================================
    // FIND SUPPLIER
    // =========================================================

    private Supplier findSupplierByName(
            String supplierName) {

        Supplier supplier =
                supplierRepository
                        .findSupplierBySupplierName(
                                supplierName
                        );

        if (supplier == null) {

            throw new VehiculeRentalException(
                    "Supplier not found"
            );
        }

        return supplier;
    }


    // =========================================================
    // FIND CLIENT SUBSCRIPTION
    // =========================================================

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
    // VALIDATE SUBSCRIPTION
    // =========================================================



    // =========================================================
    // CREATE SUBSCRIPTION
    // =========================================================

    private Subscription createSubscription(
            SubscripionInfoDTO dto,
            Client client,
            Supplier supplier) {

        Subscription subscription =
                new Subscription();

        subscription.setSubscriptionType(
                dto.getType()
        );

        subscription.setDateStart(
                Instant.now()
        );

        subscription.setSupplier(
                supplier
        );

        subscription.setClient(
                client
        );

        setSubscriptionPricing(
                subscription
        );

        return subscription;
    }


    // =========================================================
    // SET SUBSCRIPTION PRICING
    // =========================================================

    private void setSubscriptionPricing(
            Subscription subscription) {

        switch (subscription.getSubscriptionType()) {

            case BASIC:

                subscription.setPrice(
                        100.0
                );

                subscription.setReduction(
                        5
                );

                break;


            case PREMIUM:

                subscription.setPrice(
                        250.0
                );

                subscription.setReduction(
                        20
                );

                break;


            case MONTHLY:

                subscription.setPrice(
                        40.0
                );

                subscription.setReduction(
                        10
                );

                break;


            case ANNUAL:

                subscription.setPrice(
                        400.0
                );

                subscription.setReduction(
                        30
                );

                break;
        }
    }


    // =========================================================
    // UPDATE SUBSCRIPTION DATE
    // =========================================================

    private void updateSubscriptionDate(
            Subscription subscription) {

        subscription.setDateStart(
                Instant.now()
        );
    }


    // =========================================================
    // MAP SUBSCRIPTION TO DTO
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

        dto.setIdSupplier(subscription.getSupplier().getIdSupplier());


        dto.setClientEmail(
                subscription
                        .getClient()
                        .getEmail()
        );

        return dto;
    }
}