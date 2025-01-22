package org.example.expert.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public class SigninRequest {
    
    @NotBlank @Email
    private String email;
    @NotBlank
    private String password;

    public SigninRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public SigninRequest() {
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public @NotBlank String getPassword() {
        return password;
    }
}
