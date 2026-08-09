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
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Long idClient;

    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;

    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @Column(name = "budget", nullable = false)
    private Double budget;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_admin", nullable = false)
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_location", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Buying> buyings;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Ticket> tickets;
}