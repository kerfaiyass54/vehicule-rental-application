package com.projecttuto.vehicule_rental.DTO;


import com.projecttuto.vehicule_rental.enums.CategoryName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryCreation {

    private CategoryName nameCategory;
    private String typeCategory;
    private Integer stock;
}
