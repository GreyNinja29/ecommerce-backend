package com.ecommerce.user.controller;

import com.ecommerce.user.DTO.SignUpRequest;
import com.ecommerce.user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;


    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PostMapping("/add-admin")
    public ResponseEntity<?> addAdmins(@RequestBody SignUpRequest signupRequest) {
        return adminService.registerAdmin(signupRequest);
    }
}
