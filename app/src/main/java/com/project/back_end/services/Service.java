package com.project.back_end.services;


import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;


import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;



@Service
public class Service {



    private final TokenService tokenService;

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    private final DoctorService doctorService;
    private final PatientService patientService;





    // Constructor Injection
    public Service(
            TokenService tokenService,
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            DoctorService doctorService,
            PatientService patientService
    ) {

        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;

    }









    // Validate JWT token
    public Map<String,String> validateToken(
            String token,
            String role
    ) {


        try {


            boolean valid =
                    tokenService.validateToken(
                            token,
                            role
                    );



            if(!valid) {

                return Map.of(
                        "status",
                        "error",
                        "message",
                        "Unauthorized"
                );
            }




            return Map.of(
                    "status",
                    "success"
            );



        } catch(Exception e) {


            return Map.of(
                    "status",
                    "error",
                    "message",
                    "Invalid token"
            );

        }

    }









    // Validate admin login
    public Map<String,String> validateAdmin(
            String username,
            String password
    ) {



        try {


            Admin admin =
                    adminRepository
                            .findByUsername(
                                    username
                            );



            if(admin == null ||
                    !admin.getPassword()
                            .equals(password)) {


                return Map.of(
                        "status",
                        "error",
                        "message",
                        "Invalid credentials"
                );
            }






            String token =
                    tokenService.generateToken(
                            admin.getUsername(),
                            "admin"
                    );





            return Map.of(
                    "status",
                    "success",
                    "token",
                    token
            );



        } catch(Exception e) {


            return Map.of(
                    "status",
                    "error",
                    "message",
                    "Internal server error"
            );

        }

    }









    // Filter doctors
    public List<Doctor> filterDoctor(
            String name,
            String specialty,
            String time
    ) {



        if(name != null &&
                specialty != null &&
                time != null) {


            return doctorService
                    .filterDoctorsByNameSpecilityandTime(
                            name,
                            specialty,
                            time
                    );

        }






        if(name != null &&
                specialty != null) {


            return doctorService
                    .filterDoctorByNameAndSpecility(
                            name,
                            specialty
                    );

        }






        if(name != null &&
                time != null) {


            return doctorService
                    .filterDoctorByNameAndTime(
                            name,
                            time
                    );

        }






        if(specialty != null &&
                time != null) {


            return doctorService
                    .filterDoctorByTimeAndSpecility(
                            time,
                            specialty
                    );

        }






        if(name != null) {

            return doctorService
                    .findDoctorByName(
                            name
                    );
        }






        if(specialty != null) {

            return doctorService
                    .filterDoctorBySpecility(
                            specialty
                    );

        }






        if(time != null) {

            return doctorService
                    .filterDoctorsByTime(
                            time
                    );

        }





        return doctorService
                .getDoctors();

    }









    // Validate appointment
    public int validateAppointment(
            Long doctorId,
            String appointmentTime
    ) {



        Doctor doctor =
                doctorRepository
                        .findById(
                                doctorId
                        )
                        .orElse(null);




        if(doctor == null) {

            return -1;
        }






        boolean available =
                doctor.getAvailableTimes()
                        .stream()
                        .anyMatch(
                            time ->
                                time.equals(
                                    appointmentTime
                                )
                        );





        return available ? 1 : 0;

    }









    // Validate patient registration
    public boolean validatePatient(
            String email,
            String phone
    ) {



        Patient patient =
                patientRepository
                        .findByEmailOrPhone(
                                email,
                                phone
                        );



        return patient == null;

    }









    // Validate patient login
    public Map<String,String> validatePatientLogin(
            String email,
            String password
    ) {


        try {



            Patient patient =
                    patientRepository
                            .findByEmail(
                                    email
                            );





            if(patient == null ||
                    !patient.getPassword()
                            .equals(password)) {


                return Map.of(
                        "status",
                        "error",
                        "message",
                        "Invalid credentials"
                );

            }







            String token =
                    tokenService.generateToken(
                            patient.getEmail(),
                            "patient"
                    );





            return Map.of(
                    "status",
                    "success",
                    "token",
                    token
            );





        } catch(Exception e) {


            return Map.of(
                    "status",
                    "error",
                    "message",
                    "Internal server error"
            );

        }

    }









    // Filter patient appointments
    public List<?> filterPatient(
            String condition,
            String doctorName,
            String token
    ) {



        String email =
                tokenService
                        .getEmailFromToken(
                                token
                        );




        Patient patient =
                patientRepository
                        .findByEmail(
                                email
                        );





        Long patientId =
                patient.getId();







        if(condition != null &&
                doctorName != null) {


            return patientService
                    .filterByDoctorAndCondition(
                            doctorName,
                            condition,
                            patientId
                    );

        }







        if(condition != null) {


            return patientService
                    .filterByCondition(
                            patientId,
                            condition
                    );

        }







        if(doctorName != null) {


            return patientService
                    .filterByDoctor(
                            doctorName,
                            patientId
                    );

        }







        return patientService
                .getPatientAppointment(
                        patientId
                );

    }


}
