package com.projecttuto.vehicule_rental.entities;


import com.projecttuto.vehicule_rental.enums.RepairStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.Instant;

@Entity
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="repair_info")
public class RepairInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idinfo")
    private long idInfo;

    @Column(name="date_start")
    @CreatedDate
    private Instant dateStart;

    @Column(name="repair_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RepairStatus repairStatus;

    @ManyToOne
    @JoinColumn(name = "id_vehicule_repair", referencedColumnName = "idvehicule")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "id_repair_repair", referencedColumnName = "idrepair")
    private Repair repair;


}
