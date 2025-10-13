package com.ecommerce.user.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JWT jwt;

    @Autowired
    CustomUserDetails customUserDetails;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        if (request.getServletPath().startsWith("/auth/")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        String auth=request.getHeader("Authorization");

        if(auth==null || !auth.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return ;
        }

        String jwtToken=auth.substring(7);

        if(!jwtToken.isBlank() && jwt.validateToken(jwtToken)) {

            String userEmail=jwt.extractUserEmail(jwtToken);

            UserDetails userDetails=customUserDetails.loadUserByUsername(userEmail);

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

            if(userDetails!=null && authentication==null) {
                UsernamePasswordAuthenticationToken authToken=
                        new UsernamePasswordAuthenticationToken(userDetails
                        ,null
                        ,userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }


        }

        filterChain.doFilter(request,response);



    }
}
