package com.project.back_end.services;


import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;

import java.util.Date;



@Component
public class TokenService {



    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;



    @Value("${jwt.secret}")
    private String secret;





    // Constructor Injection
    public TokenService(
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository
    ) {

        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;

    }









    // Generate signing key
    private SecretKey getSigningKey() {


        return Keys.hmacShaKeyFor(
                secret.getBytes()
        );

    }









    // Generate JWT token
    public String generateToken(
            String email,
            String role
    ) {



        return Jwts.builder()

                .setSubject(email)

                .claim(
                        "role",
                        role
                )

                .setIssuedAt(
                        new Date()
                )

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                +
                                1000L * 60 * 60 * 24 * 7
                        )
                )

                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                )

                .compact();

    }









    // Extract email from token
    public String getEmailFromToken(
            String token
    ) {


        Claims claims =
                Jwts.parserBuilder()

                        .setSigningKey(
                                getSigningKey()
                        )

                        .build()

                        .parseClaimsJws(
                                token
                        )

                        .getBody();




        return claims.getSubject();

    }









    // Validate token by role
    public boolean validateToken(
            String token,
            String role
    ) {



        try {


            Claims claims =
                    Jwts.parserBuilder()

                            .setSigningKey(
                                    getSigningKey()
                            )

                            .build()

                            .parseClaimsJws(
                                    token
                            )

                            .getBody();




            String email =
                    claims.getSubject();



            String tokenRole =
                    claims.get(
                            "role",
                            String.class
                    );




            if(!role.equals(tokenRole)) {

                return false;
            }







            switch(role.toLowerCase()) {



                case "admin":

                    return adminRepository
                            .findByUsername(
                                    email
                            ) != null;





                case "doctor":

                    return doctorRepository
                            .findByEmail(
                                    email
                            ) != null;





                case "patient":

                    return patientRepository
                            .findByEmail(
                                    email
                            ) != null;




                default:

                    return false;

            }






        } catch(Exception e) {


            return false;

        }

    }



}
