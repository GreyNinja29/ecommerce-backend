package com.ecommerce.user.service;

import com.ecommerce.user.DTO.LoginRequest;
import com.ecommerce.user.DTO.Response;
import com.ecommerce.user.DTO.SignUpRequest;
import com.ecommerce.user.model.Role;
import com.ecommerce.user.model.Users;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.security.JWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWT jwt;
    @Autowired
    UserRepository repo;


    public ResponseEntity<?> verify( LoginRequest loginReq) {

        Authentication authentication=authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getEmail(),loginReq.getPassword())
        );

        if(authentication.isAuthenticated()) {
            //
            System.out.println(authentication.getCredentials());
            //
            Users user=repo.findByEmail(loginReq.getEmail())
                    .orElseThrow(()->new  UsernameNotFoundException("Email no Found"));

           String token= jwt.buildJwt(user);

            Response response=Response.builder()
                    .id(user.getUserId())
                    .jwt(token)
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .username(user.getUserName())
                    .build();


            return new ResponseEntity<>(response,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


    }

    public ResponseEntity<?> registerUser(@Valid SignUpRequest signupReq) {

        if(repo.findByEmail(signupReq.getEmail()).isPresent()) {
           return new ResponseEntity<>("Email already Registered!",HttpStatus.BAD_REQUEST);
        }
        if(repo.findByMobileNo(signupReq.getMobileNo()).isPresent()) {
            return new ResponseEntity<>("Mobile Number already Registered!",HttpStatus.BAD_REQUEST);
        }

        Users user=Users.builder()
                .userName(signupReq.getUserName())
                .role(Role.USER)
                .email(signupReq.getEmail())
                .mobileNo(signupReq.getMobileNo())
                .password(new BCryptPasswordEncoder(12).encode(signupReq.getPassword()))
                .build();

        repo.save(user);


        return new ResponseEntity<>("User registered successfully , please login",HttpStatus.CREATED);
        // could have directly logged in and returned jwt in response dto

    }
}
