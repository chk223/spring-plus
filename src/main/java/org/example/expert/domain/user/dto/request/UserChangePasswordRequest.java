package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserChangePasswordRequest {

    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

    public UserChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public UserChangePasswordRequest() {
    }

    public @NotBlank String getOldPassword() {
        return oldPassword;
    }

    public @NotBlank String getNewPassword() {
        return newPassword;
    }
}
