package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicle")
    private Long idVehicle;

    @Column(name = "vehicle_name", nullable = false, unique = true, length = 100)
    private String vehicleName;

    @Column(name = "color", nullable = false, length = 50)
    private String color;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "max_speed", nullable = false)
    private Integer maxSpeed;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission", nullable = false, length = 20)
    private Transmission transmission;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_status", nullable = false, length = 30)
    private VehiculeStatus vehicleStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_supplier", nullable = false)
    private Supplier supplier;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<RepairInfo> repairInfos;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<Buying> buyings;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<Ticket> tickets;
}