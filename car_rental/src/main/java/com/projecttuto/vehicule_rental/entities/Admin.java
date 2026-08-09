package com.projecttuto.vehicule_rental.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Long idAdmin;

    @Column(name = "admin_name", nullable = false, unique = true, length = 50)
    private String adminName;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<Client> clients;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<Repair> repairs;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<Supplier> suppliers;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private List<Location> locations;
}