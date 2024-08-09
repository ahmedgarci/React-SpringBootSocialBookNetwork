package com.example.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.function.*;
@Service
public class JwtService {

    private String secretKey="26C37E6FF9A2F54E76CB7D91C3C37";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private <T>T extractClaim(String token , Function<Claims,T> Resolver){
        Claims claims = getAllClaims(token);
        return Resolver.apply(claims);
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        return (extractUsername(token).equals(userDetails.getUsername())) && !isTokenExpired(token); 
    }

    private boolean isTokenExpired(String token){
        return ExtractExpiration(token).before(new Date());
    }

    private Date ExtractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims getAllClaims(String token){
        return Jwts.parserBuilder()
        .setSigningKey(getSignKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

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
