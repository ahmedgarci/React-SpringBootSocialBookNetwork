package com.example.demo.security;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain)
            throws ServletException, IOException {
                if(request.getServletPath().contains("/api/auth")){
                    filterChain.doFilter(request, response);
                    return ;
                }
                final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                final String jwt;
                final String userEmail;
                if( header== null || header.startsWith("Bearer ")){
                    filterChain.doFilter(request, response);
                    return ;
                }
                jwt = header.substring(7);
                userEmail = JwtService.ExtractUserEmail();
            }
    

}
