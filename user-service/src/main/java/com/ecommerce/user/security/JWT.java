package com.ecommerce.user.security;


import com.ecommerce.user.model.Users;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWT {

    @Value("${jwt.key}")
    private String key;




    public String buildJwt(Users user) {

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId",user.getUserId())
                .claim("role",user.getRole().name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*60))
                .signWith(getKey())
                .compact();


    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }






    public boolean validateToken(String jwt) {

        try {
           Date expiration= Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(jwt)
                   .getPayload()
                   .getExpiration();


           return expiration.after(new Date());


        }catch (ExpiredJwtException ex) {
            System.out.println("JWT expired: " + ex.getMessage());
        }catch (JwtException e) {
            System.out.println("Invalid jwt: "+e.getMessage());
        }

        return false;
    }

    public String extractUserEmail(String jwtToken) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();

    }

    public Long extractUserId(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .get("userId",Long.class);

    }
}
