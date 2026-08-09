package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {


    Page<Ticket> findByClient(Client client, Pageable pageable);

    Long countByClient(Client client);

    Long countByClientAndStatus(Client client, RepairDemandStatus status);

    Long countByRepair(Repair repair);

    Long countByRepairAndStatus(
            Repair repair,
            RepairDemandStatus status);

    Page<Ticket> findByRepair(Repair repair, Pageable pageable);



}
