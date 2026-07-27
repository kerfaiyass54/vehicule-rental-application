package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Client;
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

    public List<Buying> findBuyingsByClient(Client client);

    Page<Buying> findByClient(Client client, Pageable pageable);

    long countByClient(Client client);

    long countByClientAndBuyStatus(Client client, BuyStatus buyStatus);




}
