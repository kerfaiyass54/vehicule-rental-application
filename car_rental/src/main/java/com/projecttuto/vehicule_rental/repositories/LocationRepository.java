package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    public Location findLocationByName(String name);
    public List<Location> findLocationsByCountry(String country);
    public Optional<Location> findByName(String name);

}
