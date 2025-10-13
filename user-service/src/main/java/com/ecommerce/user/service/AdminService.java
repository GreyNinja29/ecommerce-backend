package com.ecommerce.user.service;


import com.ecommerce.user.DTO.SignUpRequest;
import com.ecommerce.user.DTO.UserResponse;
import com.ecommerce.user.model.Role;
import com.ecommerce.user.model.Users;
import com.ecommerce.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    UserRepository repo;


    public ResponseEntity<?> registerAdmin(SignUpRequest signupRequest) {
        if(repo.findByEmail(signupRequest.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email already Registered!", HttpStatus.BAD_REQUEST);
        }
        if(repo.findByMobileNo(signupRequest.getMobileNo()).isPresent()) {
            return new ResponseEntity<>("Mobile Number already Registered!",HttpStatus.BAD_REQUEST);
        }

        Users user=Users.builder()
                .userName(signupRequest.getUserName())
                .role(Role.ADMIN)
                .email(signupRequest.getEmail())
                .mobileNo(signupRequest.getMobileNo())
                .password(new BCryptPasswordEncoder(12).encode(signupRequest.getPassword()))
                .build();

        repo.save(user);

        return new ResponseEntity<>("Admin Registered successfully , please login",HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllUsers() {

       List<Users> allUserList= repo.findAll();

       List<UserResponse> allUserResponseList=new ArrayList<>();

       for(Users user:allUserList) {
           UserResponse userResponse=UserResponse.builder()
                           .userId(user.getUserId())
                                   .userName(user.getUserName())
                                           .email(user.getEmail())
                                                   .mobileNo(user.getMobileNo())
                   .role(user.getRole().name())

                                                           .build();
           allUserResponseList.add(userResponse);

       }

       return new ResponseEntity<>(allUserResponseList,HttpStatus.OK);

    }
}
