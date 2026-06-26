package com.project.back_end.controllers;


import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;



@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {



    private final PrescriptionService prescriptionService;
    private final Service service;
    private final AppointmentService appointmentService;




    // Constructor injection
    public PrescriptionController(
            PrescriptionService prescriptionService,
            Service service,
            AppointmentService appointmentService
    ) {
        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }









    // Save prescription
    @PostMapping("/save/{token}")
    public ResponseEntity<?> savePrescription(
            @RequestBody Prescription prescription,
            @PathVariable String token
    ) {



        Map<String,String> tokenValidation =
                service.validateToken(token, "doctor");



        if (!tokenValidation.get("status").equals("success")) {

            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }






        // Update appointment status
        appointmentService.updatePrescriptionStatus(
                prescription.getAppointmentId()
        );





        Map<String,String> result =
                prescriptionService.savePrescription(
                        prescription
                );



        return ResponseEntity.ok(result);
    }









    // Get prescription by appointment
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token
    ) {



        Map<String,String> tokenValidation =
                service.validateToken(token, "doctor");



        if (!tokenValidation.get("status").equals("success")) {

            return ResponseEntity
                    .status(401)
                    .body(tokenValidation);
        }






        Prescription prescription =
                prescriptionService.getPrescription(
                        appointmentId
                );





        if (prescription == null) {

            return ResponseEntity
                    .status(404)
                    .body(
                        Map.of(
                            "message",
                            "Prescription not found"
                        )
                    );
        }





        return ResponseEntity.ok(prescription);
    }


}
