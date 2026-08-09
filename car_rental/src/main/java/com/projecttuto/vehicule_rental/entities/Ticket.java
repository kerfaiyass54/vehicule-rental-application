package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.TicketType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long idTicket;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private TicketType type;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "date_insert", nullable = false, updatable = false)
    private Instant dateInsert;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RepairDemandStatus status;

    @Column(name = "tariff")
    private Double tariff;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_repair", nullable = false)
    private Repair repair;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicle", nullable = false)
    private Vehicule vehicle;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
    private List<Demand> demands;
}