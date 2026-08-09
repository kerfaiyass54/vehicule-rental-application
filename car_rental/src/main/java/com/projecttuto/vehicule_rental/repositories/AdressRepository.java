package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Address;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {


    public List<Address> findAddressesByLocation(Location location);

    public Page<Address> findAddressesBySupplier(Supplier supplier, Pageable pageable);

    public List<Address> findAddressesBySupplier(Supplier supplier);



}
