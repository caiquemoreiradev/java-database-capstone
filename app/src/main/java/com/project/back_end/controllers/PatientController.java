package com.project.back_end.controllers;


import com.project.back_end.models.Patient;
import com.project.back_end.models.Login;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/patient")
public class PatientController {


    private final PatientService patientService;
    private final Service service;



    // Constructor injection
    public PatientController(
            PatientService patientService,
            Service service
    ) {
        this.patientService = patientService;
        this.service = service;
    }





    // Get patient information
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatient(
            @PathVariable String token
    ) {


        Map<String,String> tokenValidation =
                service.validateToken(token, "patient");


        if (!tokenValidation.get("status").equals("success")) {

            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }


        return ResponseEntity.ok(
                patientService.getPatient(token)
        );
    }







    // Create patient
    @PostMapping("/create")
    public ResponseEntity<?> createPatient(
            @RequestBody Patient patient
    ) {


        if (service.patientExists(patient)) {

            return ResponseEntity
                    .status(409)
                    .body(
                        Map.of(
                            "message",
                            "Patient already exists"
                        )
                    );
        }



        Map<String,String> result =
                patientService.createPatient(patient);



        return ResponseEntity.ok(result);
    }









    // Patient login
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Login login
    ) {


        Map<String,String> result =
                service.validatePatientLogin(login);


        return ResponseEntity.ok(result);
    }









    // Get patient appointments
    @GetMapping("/appointments/{patientId}/{user}/{token}")
    public ResponseEntity<?> getPatientAppointment(
            @PathVariable Long patientId,
            @PathVariable String user,
            @PathVariable String token
    ) {


        Map<String,String> tokenValidation =
                service.validateToken(token, user);



        if (!tokenValidation.get("status").equals("success")) {

            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }



        return ResponseEntity.ok(
                patientService.getPatientAppointments(
                        patientId
                )
        );
    }









    // Filter patient appointments
    @GetMapping("/appointments/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token
    ) {


        Map<String,String> tokenValidation =
                service.validateToken(token, "patient");



        if (!tokenValidation.get("status").equals("success")) {

            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }



        return ResponseEntity.ok(
                service.filterPatientAppointments(
                        condition,
                        name
                )
        );
    }

}
