package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    public Client findClientByNameClient(String name);
    public Client findClientByEmail(String email);
    public Client findClientsByLocation(Location location);
    public Optional<Client> findByNameClient(String name);
    public Page<Client> findClientByNameClient(String nameClient, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Client c SET c.email = :newEmail WHERE c.email = :email")
    public void updateEmail(@Param("email") String email, @Param("newEmail") String newEmail);

    @Modifying
    @Transactional
    @Query("UPDATE Client c SET c.pass = :newPass WHERE c.email = :email")
    public void updatePassword(@Param("email") String email, @Param("newPass") String newPass);
}
