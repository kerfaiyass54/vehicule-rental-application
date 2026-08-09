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
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location")
    private Long idLocation;

    @Column(name = "location_name", nullable = false, length = 100)
    private String locationName;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "position", nullable = false, length = 255)
    private String position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_admin", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<Address> addresses;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<Client> clients;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<Repair> repairs;
}