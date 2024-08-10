package com.example.demo.Auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "firstname is mondatory")
    private String firstname;
    @NotEmpty(message = "lastname is mondatory")
    private String lastname;
    @NotEmpty(message = "email is mondatory")
    private String email;
    @NotEmpty(message = "password is mondatory")
    @Size(min = 8 , message = "password must contain at least 8 chars")
    private String password;

}
