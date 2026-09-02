package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Client;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    public Client findClientByClientName(String name);
    public Client findClientByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Client c SET c.email = :newEmail WHERE c.email = :email")
    public void updateEmail(@Param("email") String email, @Param("newEmail") String newEmail);

    Client findByEmail(String clientEmail);

    boolean existsByClientNameIgnoreCase(String trim);

    boolean existsByEmailIgnoreCase(String trim);
}
