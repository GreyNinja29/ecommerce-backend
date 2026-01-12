package com.ecommerce.order.jwt;


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
        String authHeader= request.getHeader("Authorization");


        if(authHeader!=null && authHeader.startsWith("Bearer ")) {



            String jwtToken=authHeader.substring(7);

            try {




                SecretKey key= Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

                Claims claims= Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwtToken)
                        .getPayload();


                Long userId=claims.get("userId",Long.class);
                String role=claims.get("role",String.class);
                Date expiration=claims.getExpiration();

                System.out.println("user Id:"+userId);
                System.out.println("Role:"+role);

                if(role!=null && expiration.after(new Date())) {



                    role=role.startsWith("ROLE_")?role:"ROLE_"+role;

                    SimpleGrantedAuthority authority=new SimpleGrantedAuthority(role);

                    UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            Collections.singletonList(authority)

                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);


                }



            } catch (Exception e) {


                System.out.println(e.getMessage());
                SecurityContextHolder.clearContext();
            }


        }


        filterChain.doFilter(request,response);

    }
}
