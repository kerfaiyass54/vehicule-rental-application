package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyingRepository extends JpaRepository<Buying, Long> {

    public Buying findBuyingByVehicule(Vehicule vehicule);

    public List<Buying> findBuyingsByClient(Client client);


}
