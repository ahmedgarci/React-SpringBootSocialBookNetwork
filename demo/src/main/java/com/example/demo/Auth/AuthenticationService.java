package com.example.demo.Auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Auth.Requests.AuthenticationRequest;
import com.example.demo.Auth.Requests.RegistrationRequest;
import com.example.demo.Auth.Responses.AuthenticationResponse;
import com.example.demo.Token.TokenRepository;
import com.example.demo.Token.tokenEntity;
import com.example.demo.email.emailService;
import com.example.demo.role.RoleRepository;
import com.example.demo.security.JwtService;
import com.example.demo.user.UserEntity;
import com.example.demo.user.userRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final userRepository userRepository;
    private final emailService emailService;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

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
        .expiresAt(LocalDateTime.now().plusMinutes(15)).token(generatedCode)
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


    public AuthenticationResponse authenticate(@Valid AuthenticationRequest request) {
        var authenticate = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));    
    var claims = new HashMap<String,Object>();
    var user = ((UserEntity)authenticate.getPrincipal());
    claims.put("fullname",user.getFullName());
    String token = jwtService.generateToken(claims, user);
    return AuthenticationResponse.builder().jwtToken(token).build();

}


    public void activateAccount(String code) throws MessagingException {
        tokenEntity savedToken = tokenRepository.findByToken(code)
        .orElseThrow(()-> new RuntimeException("user not found"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("code resent to the same email");
        }
        UserEntity user = userRepository.findById(savedToken.getUser().getId())
        .orElseThrow(()-> new UsernameNotFoundException("user not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
