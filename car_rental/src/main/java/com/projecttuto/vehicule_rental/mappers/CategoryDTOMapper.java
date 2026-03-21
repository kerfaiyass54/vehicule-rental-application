package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.CategoryDTO;
import com.projecttuto.vehicule_rental.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryDTOMapper {

    public CategoryDTO mapToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setIdCategory(category.getIdCategory());
        categoryDTO.setNameCategory(category.getNameCategory());
        categoryDTO.setTypeCategory(category.getTypeCategory());
        return categoryDTO;
    }
}
