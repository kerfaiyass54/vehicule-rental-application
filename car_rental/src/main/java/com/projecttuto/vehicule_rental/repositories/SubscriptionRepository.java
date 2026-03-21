package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Subscription;
import com.projecttuto.vehicule_rental.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    public List<Subscription> findSubscriptionsByClient(Client client);
    public List<Subscription> findSubscriptionsBySupplier(Supplier supplier);
    public Subscription findSubscriptionByClientAndSupplier(Client client, Supplier supplier);
}
