package com.example.demo.email;

import lombok.Getter;

@Getter
public enum EmailTemplate {
    
    ACTIVATE_ACCOUNT("activate_account");
    private final String name;

    private EmailTemplate(String name) {
        this.name = name;
    }


}
