package com.example.demo.Auth;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;



import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@AllArgsConstructor
public class AuthController {
    
    private final AuthenticationService authService;

    @RequestMapping(value="/api/auth/register",method=RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        authService.Register(request);
        return ResponseEntity.accepted().build();
        
    }
    
}
