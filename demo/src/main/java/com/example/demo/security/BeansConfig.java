package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider AuthProvider = new DaoAuthenticationProvider();
        AuthProvider.setUserDetailsService(userDetailsService);
        AuthProvider.setPasswordEncoder(PasswordEncoder());
        return AuthProvider;
    }

    @Bean
    private PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    

}
