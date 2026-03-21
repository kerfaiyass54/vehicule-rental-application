package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Repair;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
    public Repair findRepairByNameRepair(String name);
    public List<Repair> findRepairsByLocation(Location location);
    public Optional<Repair> findByNameRepair(String name);
    public Repair findRepairByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Repair r SET r.email = :newEmail WHERE r.email = :email")
    public void updateEmail(@Param("email") String email, @Param("newEmail") String newEmail);

    @Modifying
    @Transactional
    @Query("UPDATE Repair r SET r.pass = :newPass WHERE r.email = :email")
    public void updatePassword(@Param("email") String email, @Param("newPass") String newPass);




}
