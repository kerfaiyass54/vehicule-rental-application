package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.StatusRepair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {


    Page<Ticket> findByClient(Client client, Pageable pageable);

    long countByClient(Client client);

    long countByClientAndStatus(Client client, StatusRepair status);

    long countByRepair(Repair repair);

    long countByRepairAndStatus(
            Repair repair,
            StatusRepair status);

    Page<Ticket> findByRepair(Repair repair, Pageable pageable);



}
