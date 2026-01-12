package com.ecommerce.product.configs;


import com.ecommerce.product.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    JwtFilter jwtAuthFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf->csrf.disable())

                //no need to add this but as extra precaution so that it will not give login form
                .formLogin(form->form.disable())
                .httpBasic(basic -> basic.disable())


                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //work of AuthorizationFilter
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/products/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())

                //UsernamePasswordAuthenticationFilter is Lazy , does not check every request
                // by default checks specific urls (generally POST /login)

                //so in our case this filter does not run but the jwtAuthFilter for sure runs for every request



                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();


    }
}
