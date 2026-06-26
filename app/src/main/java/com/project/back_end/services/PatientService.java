package com.project.back_end.services;


import com.project.back_end.models.Patient;
import com.project.back_end.models.Appointment;

import com.project.back_end.dto.AppointmentDTO;

import com.project.back_end.repo.PatientRepository;
import com.project.back_end.repo.AppointmentRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;



@Service
public class PatientService {



    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;



    // Constructor Injection
    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService
    ) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }









    // Create patient
    public int createPatient(
            Patient patient
    ) {


        try {

            patientRepository.save(
                    patient
            );

            return 1;


        } catch(Exception e) {

            System.out.println(
                    "Error creating patient: "
                    + e.getMessage()
            );

            return 0;
        }

    }









    // Get patient appointments
    @Transactional(readOnly = true)
    public List<AppointmentDTO> getPatientAppointment(
            Long patientId
    ) {


        try {


            List<Appointment> appointments =
                    appointmentRepository
                            .findByPatientId(
                                    patientId
                            );



            return appointments
                    .stream()
                    .map(this::convertToDTO)
                    .toList();



        } catch(Exception e) {


            return List.of();

        }

    }









    // Filter by condition
    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByCondition(
            Long patientId,
            String condition
    ) {



        int status;



        if(condition.equalsIgnoreCase("future")) {

            status = 0;

        } else if(condition.equalsIgnoreCase("past")) {

            status = 1;

        } else {

            return List.of();

        }






        return appointmentRepository
                .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
                        patientId,
                        status
                )
                .stream()
                .map(this::convertToDTO)
                .toList();

    }









    // Filter by doctor
    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByDoctor(
            String doctorName,
            Long patientId
    ) {


        return appointmentRepository
                .filterByDoctorNameAndPatientId(
                        doctorName,
                        patientId
                )
                .stream()
                .map(this::convertToDTO)
                .toList();

    }









    // Filter doctor + condition
    @Transactional(readOnly = true)
    public List<AppointmentDTO> filterByDoctorAndCondition(
            String doctorName,
            String condition,
            Long patientId
    ) {



        int status;



        if(condition.equalsIgnoreCase("future")) {

            status = 0;

        } else if(condition.equalsIgnoreCase("past")) {

            status = 1;

        } else {

            return List.of();
        }






        return appointmentRepository
                .filterByDoctorNameAndPatientIdAndStatus(
                        doctorName,
                        patientId,
                        status
                )
                .stream()
                .map(this::convertToDTO)
                .toList();

    }









    // Get patient details by token
    @Transactional(readOnly = true)
    public Patient getPatientDetails(
            String token
    ) {



        try {


            String email =
                    tokenService.getEmailFromToken(
                            token
                    );



            return patientRepository
                    .findByEmail(
                            email
                    );



        } catch(Exception e) {


            return null;
        }

    }









    // Convert Appointment -> DTO
    private AppointmentDTO convertToDTO(
            Appointment appointment
    ) {


        AppointmentDTO dto =
                new AppointmentDTO();



        dto.setId(
                appointment.getId()
        );


        dto.setDoctorName(
                appointment
                .getDoctor()
                .getName()
        );


        dto.setPatientName(
                appointment
                .getPatient()
                .getName()
        );


        dto.setAppointmentTime(
                appointment
                .getAppointmentTime()
        );


        dto.setStatus(
                appointment.getStatus()
        );



        return dto;
    }


}
