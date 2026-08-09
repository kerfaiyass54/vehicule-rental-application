package com.projecttuto.vehicule_rental.entities;


import com.projecttuto.vehicule_rental.enums.ConfirmStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;


@Entity
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="demand")
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="iddemand")
    private Long idDemand;

    @Column(name="type",nullable = false)
    private String type;

    @Column(name="date_ask")
    @CreatedDate
    private Instant dateAsk;

    @Column(name="status_confirm",nullable = false)
    @Enumerated(EnumType.STRING)
    private ConfirmStatus statusConfirm;

    @Column(name="estimated_time",nullable = false)
    private int estimatedTime;


    @ManyToOne
    @JoinColumn(name = "id_ticket", referencedColumnName = "idticket")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "id_supp", referencedColumnName = "idsupp")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "id_vehicule_demand", referencedColumnName = "idvehicule")
    private Vehicule vehicule;
}
