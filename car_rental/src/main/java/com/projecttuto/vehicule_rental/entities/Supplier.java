package com.projecttuto.vehicule_rental.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;

import lombok.*;


import java.util.List;

@Entity
@Table(name = "supplier")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsupp")
    private Long idSupp;

    @Column(name = "supp_name", nullable = false, unique = true, length = 60)
    private String suppName;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "email_supp", nullable = false, unique = true)
    private String email;

    @Column(name = "pass_supp", nullable = false)
    private String pass;

    @Column(name="role", nullable = false)
    private String role;


    @Column(name = "experience")
    private int experience;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_supp", referencedColumnName = "idadmin")
    private Admin admin;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicule> vehicules;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Adress> adresses;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Demand> demands;
}