package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.StatusRepair;
import com.projecttuto.vehicule_rental.enums.TypeTicket;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idticket")
    private long idTicket;

    @Column(name="type", nullable=false)
    @Enumerated(EnumType.STRING)
    private TypeTicket type;

    @Column(name="decription", nullable=false)
    private String decription;

    @Column(name="date_insert")
    private Instant dateInsert;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private StatusRepair status;

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
