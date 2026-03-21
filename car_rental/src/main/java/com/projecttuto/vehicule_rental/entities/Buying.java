package com.projecttuto.vehicule_rental.entities;


import com.projecttuto.vehicule_rental.enums.BuyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="buyings")
public class Buying {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idbuying")
    private long idBuying;

    @Column(name="date_buy")
    private Instant dateBuy;

    @Column(name="period_buy", nullable = false)
    private int periodBuy;

    @Column(name="buy_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BuyStatus buyStatus;

    @ManyToOne
    @JoinColumn(name = "id_vehicule", referencedColumnName = "idvehicule")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "idclient")
    private Client client;




}
