package com.ecommerce.user.service;

import com.ecommerce.user.DTO.ProfileDto;
import com.ecommerce.user.DTO.ProfileUpdateDto;
import com.ecommerce.user.model.Users;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.security.JWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    JWT jwt;

    @Autowired
    UserRepository userRepository;


    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String auth=request.getHeader("Authorization");

        if(auth!=null && auth.startsWith("Bearer ")) {
            String jwtToken=auth.substring(7);

            if(!(jwtToken.isBlank()) && jwt.validateToken(jwtToken)) {
                Long userId=jwt.extractUserId(jwtToken);
                Users user=userRepository.findById(userId).orElseThrow(
                        () -> new UsernameNotFoundException("User not found"));

                ProfileDto profile=ProfileDto.builder()
                        .userName(user.getUserName())
                        .email(user.getEmail())
                        .mobileNo(user.getMobileNo())
                        .build();


                return new ResponseEntity<>(profile, HttpStatus.OK);
            }

        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    public ResponseEntity<?> updateProfile(ProfileUpdateDto update, HttpServletRequest request) {
        String auth=request.getHeader("Authorization");


        if(auth!=null && auth.startsWith("Bearer ")) {
            String jwtToken=auth.substring(7);

            if(!(jwtToken.isBlank()) && jwt.validateToken(jwtToken)) {
                Long userId=jwt.extractUserId(jwtToken);
                Users user=userRepository.findById(userId).orElseThrow(
                        () -> new UsernameNotFoundException("User not found"));

                if(update.getUserName()!=null) user.setUserName(update.getUserName());
                if(update.getEmail()!=null) user.setEmail(update.getEmail());
                if(update.getMobileNo()!=null) user.setMobileNo(update.getMobileNo());


                userRepository.save(user);


                return new ResponseEntity<>(HttpStatus.OK);
            }

        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    public ResponseEntity<?> deleteUser(HttpServletRequest request) {

        String auth=request.getHeader("Authorization");


        if(auth!=null && auth.startsWith("Bearer ")) {
            String jwtToken=auth.substring(7);

            if(!(jwtToken.isBlank()) && jwt.validateToken(jwtToken)) {
                Long userId=jwt.extractUserId(jwtToken);
                if(!userRepository.existsById(userId)) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                userRepository.deleteById(userId);


                return new ResponseEntity<>(HttpStatus.OK);
            }

        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    // to implement user service such as seeing profile ,modifying delete etc

}