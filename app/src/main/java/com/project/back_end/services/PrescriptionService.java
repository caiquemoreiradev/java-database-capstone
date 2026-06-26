package com.project.back_end.services;


import com.project.back_end.models.Prescription;

import com.project.back_end.repo.PrescriptionRepository;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Map;



@Service
public class PrescriptionService {



    private final PrescriptionRepository prescriptionRepository;



    // Constructor Injection
    public PrescriptionService(
            PrescriptionRepository prescriptionRepository
    ) {
        this.prescriptionRepository = prescriptionRepository;
    }









    // Save prescription
    public Map<String, String> savePrescription(
            Prescription prescription
    ) {


        try {



            List<Prescription> existing =
                    prescriptionRepository
                            .findByAppointmentId(
                                    prescription.getAppointmentId()
                            );





            if(existing != null &&
                    !existing.isEmpty()) {


                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Prescription already exists for this appointment"
                );
            }







            prescriptionRepository.save(
                    prescription
            );






            return Map.of(
                    "status",
                    "success",
                    "message",
                    "Prescription created"
            );





        } catch(ResponseStatusException e) {


            return Map.of(
                    "status",
                    "error",
                    "message",
                    e.getReason()
            );



        } catch(Exception e) {


            System.out.println(
                    "Error saving prescription: "
                    + e.getMessage()
            );



            return Map.of(
                    "status",
                    "error",
                    "message",
                    "Internal server error"
            );

        }

    }









    // Get prescription by appointment ID
    public Prescription getPrescription(
            Long appointmentId
    ) {


        try {


            List<Prescription> prescriptions =
                    prescriptionRepository
                            .findByAppointmentId(
                                    appointmentId
                            );




            if(prescriptions == null ||
                    prescriptions.isEmpty()) {

                return null;
            }




            return prescriptions.get(0);





        } catch(Exception e) {


            System.out.println(
                    "Error fetching prescription: "
                    + e.getMessage()
            );


            return null;

        }

    }


}
