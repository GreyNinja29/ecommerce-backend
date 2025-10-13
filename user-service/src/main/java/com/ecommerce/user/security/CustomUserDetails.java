package com.ecommerce.user.security;

import com.ecommerce.user.model.Users;
import com.ecommerce.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetails  implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users =userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email "+email));



        return User.builder()
                .username(users.getEmail())
                .password(users.getPassword())
                .roles(users.getRole().name())
                .build();

    }
}
