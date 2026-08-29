package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByClient(Client client);
    Page<Subscription> findByClient(Client client, Pageable pageable);
    Page<Subscription> findBySupplier(Supplier supplier, Pageable pageable);
    Long countBySupplier(Supplier supplier);
    boolean existsBySupplier_IdSupplierAndClient_IdClient(
            Long supplierId,
            Long clientId
    );

    Subscription
    findByClient_EmailAndSupplier_Email(
            String clientEmail,
            String supplierEmail
    );
}
