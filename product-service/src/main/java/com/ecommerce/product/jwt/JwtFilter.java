package com.ecommerce.product.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt_key}")
    private String jwtKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //get the header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //extract the token from header
        String jwtToken = authHeader.substring(7);


        try {
            //set the cryptography algorithm
            SecretKey key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();

            String role = claims.get("role", String.class);
            Date expiration=claims.getExpiration();

            if (role != null && expiration.after(new Date())) {
                // add ROLE_ prefix if it is not present cause the spring security
                //AuthorizationFilter when looking for roles finds with this prefix

                String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;

                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        Collections.singletonList(grantedAuthority)

                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();


        }


        filterChain.doFilter(request, response);


    }

}

