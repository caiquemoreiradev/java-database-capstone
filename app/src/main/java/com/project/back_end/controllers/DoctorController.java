package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {


    private final DoctorService doctorService;
    private final Service service;


    // Constructor injection
    public DoctorController(
            DoctorService doctorService,
            Service service
    ) {
        this.doctorService = doctorService;
        this.service = service;
    }



    // Get doctor availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token
    ) {


        Map<String, String> tokenValidation =
                service.validateToken(token, user);


        if (!tokenValidation.get("status").equals("success")) {
            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }


        return ResponseEntity.ok(
                doctorService.getDoctorAvailability(
                        doctorId,
                        date
                )
        );
    }




    // Get all doctors
    @GetMapping
    public ResponseEntity<?> getDoctor() {

        return ResponseEntity.ok(
                Map.of(
                        "doctors",
                        doctorService.getDoctors()
                )
        );
    }





    // Create doctor
    @PostMapping("/save/{token}")
    public ResponseEntity<?> saveDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token
    ) {


        Map<String, String> tokenValidation =
                service.validateToken(token, "admin");


        if (!tokenValidation.get("status").equals("success")) {
            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }


        Map<String,String> result =
                doctorService.saveDoctor(doctor);


        return ResponseEntity.ok(result);
    }






    // Doctor login
    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(
            @RequestBody Login login
    ) {


        Map<String,String> result =
                doctorService.login(login);


        return ResponseEntity.ok(result);
    }







    // Update doctor
    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token
    ) {


        Map<String,String> tokenValidation =
                service.validateToken(token, "admin");


        if (!tokenValidation.get("status").equals("success")) {

            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }


        Map<String,String> result =
                doctorService.updateDoctor(doctor);


        return ResponseEntity.ok(result);
    }







    // Delete doctor
    @DeleteMapping("/delete/{doctorId}/{token}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable Long doctorId,
            @PathVariable String token
    ) {


        Map<String,String> tokenValidation =
                service.validateToken(token, "admin");


        if (!tokenValidation.get("status").equals("success")) {

            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }



        Map<String,String> result =
                doctorService.deleteDoctor(doctorId);


        return ResponseEntity.ok(result);
    }








    // Filter doctors
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<?> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality
    ) {


        return ResponseEntity.ok(
                service.filterDoctors(
                        name,
                        time,
                        speciality
                )
        );
    }

}
