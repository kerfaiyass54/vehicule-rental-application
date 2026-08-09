package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.BuyStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "buyings")
public class Buying {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbuying")
    private Long idBuying;

    @Column(name = "date_buy")
    private Instant dateBuy;

    @Column(name = "period_buy", nullable = false)
    private int periodBuy;

    @Column(name = "buy_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BuyStatus buyStatus;

    @ManyToOne
    @JoinColumn(name = "id_vehicule", referencedColumnName = "idvehicule")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "idclient")
    private Client client;
}