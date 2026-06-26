package com.project.back_end.services;


import com.project.back_end.models.Doctor;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Login;

import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.AppointmentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;



@Service
public class DoctorService {



    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;



    // Constructor Injection
    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService
    ) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }







    // Get doctor availability
    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(
            Long doctorId,
            String date
    ) {


        Doctor doctor =
                doctorRepository
                        .findById(doctorId)
                        .orElse(null);



        if(doctor == null){
            return List.of();
        }



        LocalDate selectedDate =
                LocalDate.parse(date);



        LocalDateTime start =
                selectedDate.atStartOfDay();


        LocalDateTime end =
                selectedDate.atTime(LocalTime.MAX);





        List<Appointment> booked =
                appointmentRepository
                        .findByDoctorIdAndAppointmentTimeBetween(
                                doctorId,
                                start,
                                end
                        );




        List<String> bookedTimes =
                booked.stream()
                        .map(a ->
                                a.getAppointmentTime()
                                .toLocalTime()
                                .toString()
                        )
                        .toList();




        return doctor.getAvailableTimes()
                .stream()
                .filter(time ->
                        !bookedTimes.contains(time)
                )
                .toList();

    }









    // Save doctor
    @Transactional
    public int saveDoctor(
            Doctor doctor
    ) {


        try {


            if(doctorRepository
                    .findByEmail(
                        doctor.getEmail()
                    ) != null) {

                return -1;
            }



            doctorRepository.save(
                    doctor
            );


            return 1;



        } catch(Exception e){

            return 0;
        }

    }









    // Update doctor
    @Transactional
    public int updateDoctor(
            Doctor doctor
    ) {


        if(!doctorRepository
                .existsById(
                        doctor.getId()
                )) {

            return -1;
        }



        try {

            doctorRepository.save(
                    doctor
            );

            return 1;


        } catch(Exception e){

            return 0;
        }

    }









    // Get all doctors
    @Transactional(readOnly = true)
    public List<Doctor> getDoctors() {

        return doctorRepository.findAll();

    }









    // Delete doctor
    @Transactional
    public int deleteDoctor(
            Long doctorId
    ) {


        if(!doctorRepository
                .existsById(
                        doctorId
                )) {

            return -1;
        }



        try {


            appointmentRepository
                    .deleteAllByDoctorId(
                            doctorId
                    );



            doctorRepository.deleteById(
                    doctorId
            );


            return 1;



        } catch(Exception e){

            return 0;
        }

    }









    // Doctor login validation
    public Map<String,String> validateDoctor(
            Login login
    ) {


        Doctor doctor =
                doctorRepository
                        .findByEmail(
                                login.getEmail()
                        );



        if(doctor == null ||
                !doctor.getPassword()
                        .equals(
                            login.getPassword()
                        )) {


            return Map.of(
                    "status",
                    "error",
                    "message",
                    "Invalid credentials"
            );
        }




        String token =
                tokenService.generateToken(
                        doctor.getId(),
                        "doctor"
                );



        return Map.of(
                "status",
                "success",
                "token",
                token
        );

    }









    // Find doctor by name
    @Transactional(readOnly = true)
    public List<Doctor> findDoctorByName(
            String name
    ) {


        return doctorRepository
                .findByNameLike(
                        name
                );

    }









    // Filter name + specialty + time
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorsByNameSpecilityandTime(
            String name,
            String specialty,
            String time
    ) {


        List<Doctor> doctors =
                doctorRepository
                        .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                                name,
                                specialty
                        );



        return filterDoctorByTime(
                doctors,
                time
        );

    }









    // Filter by time
    public List<Doctor> filterDoctorByTime(
            List<Doctor> doctors,
            String time
    ) {


        return doctors.stream()
                .filter(d ->
                        d.getAvailableTimes()
                         .stream()
                         .anyMatch(slot ->
                                 slot.toLowerCase()
                                 .contains(
                                     time.toLowerCase()
                                 )
                         )
                )
                .toList();

    }









    // Name + time
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByNameAndTime(
            String name,
            String time
    ) {


        return filterDoctorByTime(
                findDoctorByName(name),
                time
        );

    }









    // Name + specialty
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByNameAndSpecility(
            String name,
            String specialty
    ) {


        return doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        name,
                        specialty
                );

    }









    // Time + specialty
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByTimeAndSpecility(
            String time,
            String specialty
    ) {


        return filterDoctorByTime(
                doctorRepository
                    .findBySpecialtyIgnoreCase(
                            specialty
                    ),
                time
        );

    }









    // Specialty only
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorBySpecility(
            String specialty
    ) {


        return doctorRepository
                .findBySpecialtyIgnoreCase(
                        specialty
                );

    }









    // All doctors by time
    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorsByTime(
            String time
    ) {


        return filterDoctorByTime(
                doctorRepository.findAll(),
                time
        );

    }


}
