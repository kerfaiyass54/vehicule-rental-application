package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Repair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
    public Repair findRepairByRepairName(String name);
    public Optional<Repair> findByRepairName(String name);
    public Repair findRepairByEmail(String email);






}
