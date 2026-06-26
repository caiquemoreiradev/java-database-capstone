package com.project.back_end.repo;


import com.project.back_end.models.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;



@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {



    // Find appointments for a doctor in a time range
    @Query("""
        SELECT a 
        FROM Appointment a
        LEFT JOIN FETCH a.doctor d
        LEFT JOIN FETCH d.availableTimes
        WHERE d.id = :doctorId
        AND a.appointmentTime BETWEEN :start AND :end
    """)
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );







    // Find appointments by doctor and patient name in time range
    @Query("""
        SELECT a
        FROM Appointment a
        LEFT JOIN FETCH a.doctor d
        LEFT JOIN FETCH a.patient p
        WHERE d.id = :doctorId
        AND LOWER(p.name) LIKE LOWER(CONCAT('%', :patientName, '%'))
        AND a.appointmentTime BETWEEN :start AND :end
    """)
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("patientName") String patientName,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );







    // Delete all appointments by doctor
    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);







    // Find all appointments for patient
    List<Appointment> findByPatientId(
            Long patientId
    );







    // Find patient appointments by status ordered by time
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
            Long patientId,
            int status
    );







    // Filter appointments by doctor name and patient
    @Query("""
        SELECT a
        FROM Appointment a
        JOIN a.doctor d
        WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
        AND a.patient.id = :patientId
    """)
    List<Appointment> filterByDoctorNameAndPatientId(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId
    );







    // Filter appointments by doctor, patient and status
    @Query("""
        SELECT a
        FROM Appointment a
        JOIN a.doctor d
        WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
        AND a.patient.id = :patientId
        AND a.status = :status
    """)
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId,
            @Param("status") int status
    );







    // Update appointment status
    @Modifying
    @Transactional
    @Query("""
        UPDATE Appointment a
        SET a.status = :status
        WHERE a.id = :id
    """)
    void updateStatus(
            @Param("status") int status,
            @Param("id") long id
    );


}
