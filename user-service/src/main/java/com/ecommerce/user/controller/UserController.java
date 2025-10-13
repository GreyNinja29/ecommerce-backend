package com.ecommerce.user.controller;

import com.ecommerce.user.DTO.ProfileUpdateDto;
import com.ecommerce.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/v")
    public ResponseEntity<?> testMethode() {
        return new ResponseEntity<>("Testing with jwt", HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> profile(HttpServletRequest req) {
        return userService.getProfile(req);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileUpdateDto update, HttpServletRequest req) {
        return userService.updateProfile(update,req);
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteProfile(HttpServletRequest request) {
        return userService.deleteUser(request);
    }


    // To Implement refresh tokens and invalidation of tokens
}
