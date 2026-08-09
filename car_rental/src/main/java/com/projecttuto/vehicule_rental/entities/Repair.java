package com.projecttuto.vehicule_rental.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="repair")
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idrepair")
    private Long idRepair;

    @Column(name="name_repair", nullable = false, unique = true)
    private String nameRepair;

    @Column(name="role", nullable = false)
    private String role;



    @Column(name="email_repair", nullable = false, unique = true)
    private String email;

    @Column(name="pass_repair", nullable = false)
    private String pass;

    @ManyToOne
    @JoinColumn(name= "id_location",referencedColumnName = "idloc")
    private Location location;


    @ManyToOne
    @JoinColumn(name = "id_admin_rep", referencedColumnName = "idadmin")
    private Admin admin;

    @OneToMany(mappedBy = "repair", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "repair", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RepairInfo> repairInfos;

}
