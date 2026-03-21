package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Adress;
import com.projecttuto.vehicule_rental.entities.Supplier;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    public Supplier findSupplierBySuppName(String name);
    public Optional<Supplier> findBySuppName(String name);
    public Supplier findSupplierByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Supplier s SET s.email = :newEmail WHERE s.email = :email")
    public void updateEmail(@Param("email") String email, @Param("newEmail") String newEmail);

    @Modifying
    @Transactional
    @Query("UPDATE Supplier s SET s.pass = :newPass WHERE s.email = :email")
    public void updatePassword(@Param("email") String email, @Param("newPass") String newPass);


}
