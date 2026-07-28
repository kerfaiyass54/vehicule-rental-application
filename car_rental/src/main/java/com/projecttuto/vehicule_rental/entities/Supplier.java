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

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "supplier")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsupp")
    private Long idSupp;

    @Column(name = "supp_name", nullable = false, unique = true)
    private String suppName;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "email_supp", nullable = false, unique = true)
    private String email;

    @Column(name = "pass_supp", nullable = false)
    private String pass;

    @Column(name = "experience")
    private int experience;

    @Column(name = "role_user")
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin_supp", referencedColumnName = "idadmin")
    @ToString.Exclude
    private Admin admin;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Vehicule> vehicules;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Adress> adresses;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Demand> demands;
}