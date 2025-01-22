package org.example.expert.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserNicknameChangeRequest {
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public UserNicknameChangeRequest() {
    }

    public UserNicknameChangeRequest(String nickname) {
        this.nickname = nickname;
    }
}
