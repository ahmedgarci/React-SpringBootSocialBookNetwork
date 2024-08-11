package com.example.demo.Auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Token.TokenRepository;
import com.example.demo.Token.tokenEntity;
import com.example.demo.email.emailService;
import com.example.demo.role.RoleRepository;
import com.example.demo.user.UserEntity;
import com.example.demo.user.userRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final userRepository userRepository;
    private final emailService emailService;
    private final TokenRepository tokenRepository;

    public void Register(RegistrationRequest request) throws MessagingException{
        var role =  roleRepository.findByName("USER")
        .orElseThrow(()->new IllegalStateException("role user was not found") );
        var user  = UserEntity.builder()
        .email(request.getEmail()).firstname(request.getFirstname())
        .lastname(request.getLastname()).createdDate(LocalDateTime.now())
        .password(new BCryptPasswordEncoder().encode(request.getPassword()))
        .accountLocked(false).enabled(false)
        .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }


    private void sendValidationEmail(UserEntity user) throws MessagingException {
        var code = generateAndSaveActivationToken(user);
        emailService.SendEmail(user.getEmail(), 
        user.getFullName(), null,
            "http://localhost:4200/activate-account", 
            code, "Account Activation");
    }


    private String generateAndSaveActivationToken(UserEntity user){
        String generatedCode = generateRandomCode(6);
        tokenEntity token = tokenEntity.builder().issuedAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusMinutes(15)).Token(generatedCode)
        .user(user)
        .build();
        tokenRepository.save(token);        
        return generatedCode;
    }

    private String generateRandomCode(int length){
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
