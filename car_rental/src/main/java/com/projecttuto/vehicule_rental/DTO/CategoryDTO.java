package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.CategoryName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CategoryDTO {

    private long idCategory;
    private CategoryName nameCategory;
    private String typeCategory;
    private Integer stock;
    private String supplierEmail;

}
