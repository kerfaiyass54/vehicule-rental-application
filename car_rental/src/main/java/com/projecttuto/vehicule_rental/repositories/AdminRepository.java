package com.projecttuto.vehicule_rental.repositories;

import com.projecttuto.vehicule_rental.entities.Admin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {



    public Admin findAdminByIdAdmin(Long idAdmin);


    @Modifying
    @Transactional
    @Query("UPDATE Admin a SET a.passwordHash = :newPass WHERE a.email = :email")
    public void updatePassword(@Param("email") String email, @Param("newPass") String newPass);

    Admin findAdminByEmail(String email);
}
