package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.CategoryName;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcategory")
    private Long idCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "name_category", nullable = false)
    private CategoryName nameCategory;

    @Column(name = "type_category", nullable = false, unique = true)
    private String typeCategory;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Vehicule> vehicules;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_supp_cat", referencedColumnName = "idsupp")
    @ToString.Exclude
    private Supplier supplier;

}