package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.AddressStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_address")
    private Long idAddress;

    @Column(name="road",nullable = false, length = 60)
    private String road;

    @Column(name="number",nullable = false)
    private int number;

    @Enumerated(EnumType.STRING)
    @Column(name="adress_status",nullable = false)
    private AddressStatus addressStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_supp", referencedColumnName = "idsupp")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idloc", referencedColumnName = "idloc")
    private Location location;






}
