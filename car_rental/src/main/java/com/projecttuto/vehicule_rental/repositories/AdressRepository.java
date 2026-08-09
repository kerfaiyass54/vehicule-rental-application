package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Adress;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdressRepository extends JpaRepository<Adress, Long> {


    public List<Adress> findAdressesByLocation(Location location);

    public Page<Adress> findAdressesBySupplier(Supplier supplier, Pageable pageable);

    public List<Adress> findAdressesBySupplier(Supplier supplier);



}
