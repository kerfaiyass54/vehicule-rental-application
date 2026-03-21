package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="vehicule")
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idvehicule")
    private Long idVehicule;

    @Column(name="name_vehicule", nullable=false, unique=true)
    private String nameVehicule;

    @Column(name="color", nullable=false)
    private String color;

    @Column(name="brand", nullable = false)
    private String brand;

    @Column(name="price", nullable = false)
    private double price;

    @Column(name="high_speed", nullable = false)
    private int highSpeed;

    @Column(name="transmission", nullable = false)
    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @Column(name="vehicule_status")
    @Enumerated(EnumType.STRING)
    private VehiculeStatus vehiculeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_supp", referencedColumnName = "idsupp")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", referencedColumnName = "idcategory")
    private Category category;

    @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RepairInfo> repairInfos;

    @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Buying> buyings;

    @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets;





}
