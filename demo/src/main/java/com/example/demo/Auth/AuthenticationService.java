package com.example.demo.Auth;

import java.security.SecureRandom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.role.RoleRepository;
import com.example.demo.user.UserEntity;
import com.example.demo.user.userRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final userRepository userRepository;

    public void Register(RegistrationRequest request){
        var role =  roleRepository.findByName("USER")
        .orElseThrow(()->new IllegalStateException("role user was not found") );
        var user  = UserEntity.builder()
        .email(request.getEmail()).firstname(request.getFirstname())
        .lastname(request.getLastname())
        .password(new BCryptPasswordEncoder().encode(request.getPassword()))
        .accoundLocked(false).enabled(false)
        .build();
        userRepository.save(user);
    }

    private String generateAndSaveActivationToken(UserEntity user){
        var generatedToken = generateToken(6);

        return "";
    }

    private String generateToken(int length){
        String caracters = "1234567890";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom(); 
        for(int i=0;i<length;i++){
            int randomIndex = secureRandom.nextInt(caracters.length());
            codeBuilder.append(caracters.charAt(randomIndex)); 
        }
        return codeBuilder.toString();
    }




}
