package com.projecttuto.vehicule_rental.entities;


import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.Instant;
import java.time.LocalTime;

@Entity
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="demand")
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="iddemand")
    private long idDemand;

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
