package com.example.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    private String secretKey="26C37E6FF9A2F54E76CB7D91C3C37";

    public String generateToken(UserDetails userdetails){
        return generateToken(new HashMap<>(),userdetails);
    }

    public String generateToken(HashMap<String ,Object> claims,UserDetails userDetails){
        return BuildToken(claims,userDetails);
    }

    private String BuildToken(HashMap<String, Object> claims, UserDetails userDetails) {
        var authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .toList();
        return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setSubject(userDetails.getUsername())      
        .claim("authorities", authorities)
        .signWith(getSignKey())  
        .compact();
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
