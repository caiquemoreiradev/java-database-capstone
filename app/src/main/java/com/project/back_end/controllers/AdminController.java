package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    private final Service service;


    // Constructor injection
    public AdminController(Service service) {
        this.service = service;
    }


    // Admin login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(
            @RequestBody Admin admin
    ) {

        Map<String, String> response = service.validateAdmin(admin);

        return ResponseEntity.ok(response);
    }
}
