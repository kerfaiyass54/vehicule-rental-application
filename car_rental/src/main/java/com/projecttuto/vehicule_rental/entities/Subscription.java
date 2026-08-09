package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.SubscriptionType;
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subscription")
    private Long idSubscription;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false, length = 30)
    private SubscriptionType subscriptionType;

    @CreatedDate
    @Column(name = "date_start", nullable = false, updatable = false)
    private Instant dateStart;

    @Column(name = "reduction", nullable = false)
    private Integer reduction;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_supplier", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;
}