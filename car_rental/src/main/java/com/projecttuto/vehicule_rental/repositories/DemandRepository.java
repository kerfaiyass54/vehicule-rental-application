package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Demand;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Ticket;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Long> {
    Demand findDemandByTicket(Ticket ticket);
    Long countByTicketRepair(Repair repair);

    Long countByTicketRepairAndStatusConfirm(
            Repair repair,
            ConfirmStatus statusConfirm);

    Page<Demand> findBySupplier(Supplier supplier, Pageable pageable);
    Long countBySupplier(Supplier supplier);

    Long countBySupplierAndStatusConfirm(
            Supplier supplier,
            ConfirmStatus statusConfirm);


}
