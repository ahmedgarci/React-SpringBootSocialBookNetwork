package com.example.demo.Auth;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Auth.Requests.AuthenticationRequest;
import com.example.demo.Auth.Requests.RegistrationRequest;
import com.example.demo.Auth.Responses.AuthenticationResponse;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@AllArgsConstructor
public class AuthController {
    
    private final AuthenticationService authService;

    @RequestMapping(value="/api/auth/register",method=RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        authService.Register(request);
        return ResponseEntity.accepted().build();
        
    }

    @RequestMapping(value = "/api/auth/authenticate", method=RequestMethod.POST)
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
    
    @GetMapping("/api/auth/activate_account")
    public void confirm(@RequestParam String code) throws MessagingException {
        authService.activateAccount(code);
    }
    

    


}
