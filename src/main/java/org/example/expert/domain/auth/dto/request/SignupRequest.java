package org.example.expert.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SignupRequest {

    @NotBlank @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
    @NotBlank
    private String userRole;

    public SignupRequest(String email, String password, String nickname, String userRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    public SignupRequest() {
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public @NotBlank String getNickname() {
        return nickname;
    }

    public @NotBlank String getUserRole() {
        return userRole;
    }
}
