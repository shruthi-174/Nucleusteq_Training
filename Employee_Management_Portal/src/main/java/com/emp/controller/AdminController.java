package com.emp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.emp.dto.UserRequest;
import com.emp.entities.Project;
import com.emp.service.AdminService;
import com.emp.service.UserService;

@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        try {
            boolean user = adminService.registerUser(userRequest);
            return ResponseEntity.ok(user); 
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
