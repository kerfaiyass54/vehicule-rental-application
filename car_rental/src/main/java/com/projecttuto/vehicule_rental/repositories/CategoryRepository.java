package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Category;
import com.projecttuto.vehicule_rental.enums.CategoryName;
import com.projecttuto.vehicule_rental.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Category findCategoriesByNameCategory(CategoryName nameCategory);

    public Category findCategoryByTypeCategory(String categoryType);

}
