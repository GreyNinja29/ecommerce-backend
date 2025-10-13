package com.ecommerce.user.controller;

import com.ecommerce.user.DTO.LoginRequest;
import com.ecommerce.user.DTO.SignUpRequest;
import com.ecommerce.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginReq) {

        return authService.verify(loginReq);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signupReq) {

        return authService.registerUser(signupReq);
    }




}
