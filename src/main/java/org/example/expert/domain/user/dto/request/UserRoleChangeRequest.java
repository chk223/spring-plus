package org.example.expert.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserRoleChangeRequest {

    private String role;

    public String getRole() {
        return role;
    }

    public UserRoleChangeRequest() {
    }

    public UserRoleChangeRequest(String role) {
        this.role = role;
    }
}
