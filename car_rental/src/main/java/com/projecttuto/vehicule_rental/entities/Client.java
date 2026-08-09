package com.projecttuto.vehicule_rental.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idclient")
    private long idClient;

    @Column(name="name_client", nullable = false, length = 50)
    private String nameClient;

    @Column(name="role", nullable = false)
    private String role;


    @Column(name="budget", nullable = false)
    private double budget;

    @Column(name="nationality")
    private String nationality;

    @Column(name="age", nullable = false)
    private int age;

    @Column(name="email_client",nullable = false, unique = true)
    private String email;

    @Column(name="pass_client",nullable = false)
    private String pass;

    @ManyToOne
    @JoinColumn(name = "id_admin_client", referencedColumnName = "idadmin")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "id_location", referencedColumnName = "idloc")
    private Location location;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Buying> buyings;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets;



}
