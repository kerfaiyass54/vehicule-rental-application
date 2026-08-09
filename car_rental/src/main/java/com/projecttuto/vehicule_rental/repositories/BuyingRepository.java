package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.BuyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyingRepository extends JpaRepository<Buying, Long> {

    public Buying findBuyingByVehicule(Vehicule vehicule);


    Page<Buying> findByClient(Client client, Pageable pageable);

    Long countByClient(Client client);

    Long countByClientAndBuyStatus(Client client, BuyStatus buyStatus);
    Page<Buying> findByVehiculeSupplier(Supplier supplier, Pageable pageable);
    Long countByVehiculeSupplier(Supplier supplier);

    Long countByVehiculeSupplierAndBuyStatus(
            Supplier supplier,
            BuyStatus buyStatus);



}
