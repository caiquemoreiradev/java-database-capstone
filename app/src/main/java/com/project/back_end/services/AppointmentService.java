package com.project.back_end.services;


import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;

import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;



@Service
public class AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final Service service;
    private final TokenService tokenService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;



    // Constructor injection
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            Service service,
            TokenService tokenService,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.service = service;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }







    // Book appointment
    @Transactional
    public Map<String,String> bookAppointment(
            Appointment appointment
    ) {


        try {

            Doctor doctor =
                    doctorRepository.findById(
                            appointment.getDoctor().getId()
                    ).orElse(null);



            if (doctor == null) {

                return Map.of(
                        "status",
                        "error",
                        "message",
                        "Doctor not found"
                );
            }




            appointment.setDoctor(doctor);


            appointmentRepository.save(
                    appointment
            );



            return Map.of(
                    "status",
                    "success",
                    "message",
                    "Appointment booked"
            );


        } catch(Exception e) {


            return Map.of(
                    "status",
                    "error",
                    "message",
                    "Could not book appointment"
            );
        }
    }









    // Update appointment
    @Transactional
    public Map<String,String> updateAppointment(
            Appointment appointment
    ) {


        Appointment existing =
                appointmentRepository
                        .findById(
                            appointment.getId()
                        )
                        .orElse(null);



        if(existing == null){

            return Map.of(
                    "status",
                    "error",
                    "message",
                    "Appointment not found"
            );
        }



        if(!existing.getPatient()
                .getId()
                .equals(
                    appointment.getPatient().getId()
                )) {


            return Map.of(
                    "status",
                    "error",
                    "message",
                    "Patient not authorized"
            );
        }



        existing.setAppointmentTime(
                appointment.getAppointmentTime()
        );


        appointmentRepository.save(
                existing
        );


        return Map.of(
                "status",
                "success",
                "message",
                "Appointment updated"
        );
    }









    // Cancel appointment
    @Transactional
    public Map<String,String> cancelAppointment(
            Long appointmentId
    ) {


        Appointment appointment =
                appointmentRepository
                        .findById(appointmentId)
                        .orElse(null);



        if(appointment == null){

            return Map.of(
                    "status",
                    "error",
                    "message",
                    "Appointment not found"
            );
        }




        appointmentRepository.delete(
                appointment
        );



        return Map.of(
                "status",
                "success",
                "message",
                "Appointment cancelled"
        );
    }









    // Get appointments
    @Transactional(readOnly = true)
    public List<Appointment> getAppointments(
            String date,
            String patientName
    ) {


        LocalDate selectedDate =
                LocalDate.parse(date);



        LocalDateTime start =
                selectedDate.atStartOfDay();


        LocalDateTime end =
                selectedDate.atTime(
                        LocalTime.MAX
                );




        if(patientName == null ||
                patientName.isEmpty()) {


            return appointmentRepository
                    .findByDoctorIdAndAppointmentTimeBetween(
                            null,
                            start,
                            end
                    );
        }




        return appointmentRepository
                .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                        null,
                        patientName,
                        start,
                        end
                );
    }









    // Change appointment status
    @Transactional
    public void changeStatus(
            int status,
            long id
    ) {


        appointmentRepository.updateStatus(
                status,
                id
        );

    }







    // Update when prescription is created
    @Transactional
    public void updatePrescriptionStatus(
            Long appointmentId
    ) {


        appointmentRepository.updateStatus(
                2,
                appointmentId
        );

    }

}
