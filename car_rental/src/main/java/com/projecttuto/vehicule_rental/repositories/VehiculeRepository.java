package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    public Vehicule findVehiculeByNameVehicule(String name);
    public Vehicule findVehiculeByIdVehicule(Long id);

    public Page<Vehicule> findVehiculesBySupplier(Supplier supplier, Pageable pageable);

    public List<Vehicule> findVehiculesBySupplier(Supplier supplier);
}
