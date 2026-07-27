package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    public Vehicule findVehiculeByNameVehicule(String name);
    public Vehicule findVehiculeByIdVehicule(Long id);

    public Page<Vehicule> findVehiculesBySupplier(Supplier supplier, Pageable pageable);

    public List<Vehicule> findVehiculesBySupplier(Supplier supplier);
    Optional<Vehicule> findBynameVehicule(String name);

    @Query("""
        SELECT v
        FROM Vehicule v
        WHERE
            (:keyword IS NULL OR
             LOWER(v.nameVehicule) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(v.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(v.color) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:transmission IS NULL OR v.transmission = :transmission)
        AND (:status IS NULL OR v.vehiculeStatus = :status)
        AND (:minPrice IS NULL OR v.price >= :minPrice)
        AND (:maxPrice IS NULL OR v.price <= :maxPrice)
    """)
    Page<Vehicule> searchVehicules(
            @Param("keyword") String keyword,
            @Param("transmission") Transmission transmission,
            @Param("status") VehiculeStatus status,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);
}
