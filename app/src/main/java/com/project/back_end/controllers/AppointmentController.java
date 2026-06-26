package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {


    private final AppointmentService appointmentService;
    private final Service service;


    // Constructor injection
    public AppointmentController(
            AppointmentService appointmentService,
            Service service
    ) {
        this.appointmentService = appointmentService;
        this.service = service;
    }


    // Get appointments by date and patient
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token
    ) {

        Map<String, String> tokenValidation =
                service.validateToken(token, "doctor");


        if (!tokenValidation.get("status").equals("success")) {
            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }


        return ResponseEntity.ok(
                appointmentService.getAppointments(date, patientName)
        );
    }



    // Book appointment
    @PostMapping("/book/{token}")
    public ResponseEntity<?> bookAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token
    ) {


        Map<String, String> tokenValidation =
                service.validateToken(token, "patient");


        if (!tokenValidation.get("status").equals("success")) {
            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }


        Map<String, String> result =
                appointmentService.bookAppointment(appointment);


        return ResponseEntity.ok(result);
    }



    // Update appointment
    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateAppointment(
            @RequestBody Appointment appointment,
            @PathVariable String token
    ) {


        Map<String, String> tokenValidation =
                service.validateToken(token, "patient");


        if (!tokenValidation.get("status").equals("success")) {
            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }


        Map<String, String> result =
                appointmentService.updateAppointment(appointment);


        return ResponseEntity.ok(result);
    }



    // Cancel appointment
    @DeleteMapping("/cancel/{appointmentId}/{token}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId,
            @PathVariable String token
    ) {


        Map<String, String> tokenValidation =
                service.validateToken(token, "patient");


        if (!tokenValidation.get("status").equals("success")) {
            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }


        Map<String, String> result =
                appointmentService.cancelAppointment(appointmentId);


        return ResponseEntity.ok(result);
    }

}
