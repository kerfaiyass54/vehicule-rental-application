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

    public Admin findAdminByAdminName(String adminName);

    public Admin findAdminByIdAdmin(long idAdmin);

    public Admin findAdminByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Admin a SET a.email = :newEmail WHERE a.email = :email")
    public void updateEmail(@Param("email") String email, @Param("newEmail") String newEmail);

    @Modifying
    @Transactional
    @Query("UPDATE Admin a SET a.pass = :newPass WHERE a.email = :email")
    public void updatePassword(@Param("email") String email, @Param("newPass") String newPass);

}
