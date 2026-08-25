package com.projecttuto.vehicule_rental.specifications;

import com.projecttuto.vehicule_rental.entities.Supplier;
import org.springframework.data.jpa.domain.Specification;

public final class SupplierSpecification {

    private SupplierSpecification() {
    }

    public static Specification<Supplier> search(String keyword) {

        return (root, query, criteriaBuilder) -> {

            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchValue = "%" + keyword.trim().toLowerCase() + "%";

            return criteriaBuilder.or(

                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("suppName")),
                            searchValue
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("nationality")),
                            searchValue
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("email")),
                            searchValue
                    )
            );
        };
    }
}