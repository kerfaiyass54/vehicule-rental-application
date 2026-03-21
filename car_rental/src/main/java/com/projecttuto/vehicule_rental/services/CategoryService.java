package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.entities.Category;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.CategoryName;

import java.util.List;

public interface CategoryService {


    List<Vehicule> getVehiculesByName(CategoryName name);
    List<String> getCategoryTypes(CategoryName name);
    void addCategory(Category category, String nameSupplier);
    void deleteCategory(Category category);



}
