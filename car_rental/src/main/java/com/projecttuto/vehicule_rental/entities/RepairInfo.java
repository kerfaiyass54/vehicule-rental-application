package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.RepairStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "repair_infos")
public class RepairInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repair_info")
    private Long idRepairInfo;

    @Column(name = "date_start", nullable = false, updatable = false)
    private Instant dateStart;

    @Enumerated(EnumType.STRING)
    @Column(name = "repair_status", nullable = false, length = 30)
    private RepairStatus repairStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vehicle", nullable = false)
    private Vehicule vehicle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_repair", nullable = false)
    private Repair repair;
}