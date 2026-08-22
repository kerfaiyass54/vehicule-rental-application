package com.projecttuto.vehicule_rental.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_supplier")
    private Long idSupplier;

    @Column(name = "supplier_name", nullable = false, unique = true, length = 100)
    private String supplierName;

    @Column(name = "nationality", nullable = false, length = 100)
    private String nationality;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @Column(name = "experience")
    private Integer experience;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_admin", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<Vehicule> vehicles;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<Address> addresses;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<Demand> demands;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private List<Buying> buyings;
}