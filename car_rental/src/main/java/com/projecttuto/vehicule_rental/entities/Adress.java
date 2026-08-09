package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.AdressStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="adress")
public class Adress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idadress")
    private Long idAdress;

    @Column(name="road",nullable = false, length = 60)
    private String road;

    @Column(name="number",nullable = false)
    private int number;

    @Enumerated(EnumType.STRING)
    @Column(name="adress_status",nullable = false)
    private AdressStatus adressStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idsupp", referencedColumnName = "idsupp")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idloc", referencedColumnName = "idloc")
    private Location location;






}
