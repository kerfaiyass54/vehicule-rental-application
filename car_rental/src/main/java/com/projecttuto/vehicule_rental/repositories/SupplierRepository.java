package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
    public Supplier findSupplierByEmail(String email);

    @Query("""
    SELECT s
    FROM Supplier s
    JOIN Subscription sub
        ON sub.supplier = s
    WHERE sub.client.idClient = :clientId
""")
    List<Supplier> findSubscribedSuppliers(@Param("clientId") Long clientId);


    Supplier findSupplierBySupplierName(String supplierName);
}
