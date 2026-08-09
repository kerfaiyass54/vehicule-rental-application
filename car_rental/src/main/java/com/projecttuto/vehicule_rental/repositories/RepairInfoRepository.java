package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RepairInfoRepository extends JpaRepository<RepairInfo, Long> {
    Page<RepairInfo> findByRepair(Repair repair, Pageable pageable);


    Long countByRepairAndRepairStatus(
            Repair repair,
            RepairStatus repairStatus);


    RepairInfo findByVehicule(Vehicule vehicule);
    Long countByRepairStatus(RepairStatus repairStatus);

}
