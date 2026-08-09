package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.TicketType;
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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idticket")
    private Long idTicket;

    @Column(name="type", nullable=false)
    @Enumerated(EnumType.STRING)
    private TicketType type;

    @Column(name="decription", nullable=false)
    private String decription;

    @Column(name="date_insert")
    private Instant dateInsert;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private RepairDemandStatus status;

    @Column(name="tarif")
    private float tarif;

    @ManyToOne
    @JoinColumn(name = "id_rep", referencedColumnName = "idrepair")
    private Repair repair;

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "idclient")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_vehicule_ticket", referencedColumnName = "idvehicule")
    private Vehicule vehicule;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Demand> demands;



}
