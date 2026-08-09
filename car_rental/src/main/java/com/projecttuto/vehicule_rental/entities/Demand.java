package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "demands")
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demand")
    private Long idDemand;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @CreatedDate
    @Column(name = "date_ask", nullable = false, updatable = false)
    private Instant dateAsk;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_confirm", nullable = false, length = 30)
    private ConfirmStatus statusConfirm;

    @Column(name = "estimated_time", nullable = false)
    private Integer estimatedTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_supplier", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicle", nullable = false)
    private Vehicule vehicle;
}