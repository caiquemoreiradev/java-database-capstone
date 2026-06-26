package com.project.back_end.repo;


import com.project.back_end.models.Patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {




    // Find patient by email
    Patient findByEmail(
            String email
    );






    // Find patient by email or phone
    @Query("""
        SELECT p
        FROM Patient p
        WHERE p.email = :email
        OR p.phone = :phone
    """)
    Patient findByEmailOrPhone(
            @Param("email") String email,
            @Param("phone") String phone
    );


}
