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
@Table(name = "repairs")
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repair")
    private Long idRepair;

    @Column(name = "repair_name", nullable = false, unique = true, length = 100)
    private String repairName;

    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_location", nullable = false)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_admin", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "repair", fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "repair", fetch = FetchType.LAZY)
    private List<RepairInfo> repairInfos;
}