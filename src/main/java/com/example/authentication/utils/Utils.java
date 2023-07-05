package com.example.authentication.utils;

import io.jsonwebtoken.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

@Configuration
public class Utils {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    static int tokenValidity = 1000000;
    static String secretKey = "this is secret key";

    public static String generateToken(String email, int id)  {

        return Jwts.builder().setSubject(email).claim("id", id).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + tokenValidity))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public static String decodeToken(String token){

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody()
                .getSubject();
    }
}
