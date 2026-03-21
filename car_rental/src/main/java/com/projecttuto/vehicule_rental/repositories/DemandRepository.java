package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Long> {
    public List<Demand> findDemandsByDateAsk(Instant date);

}
