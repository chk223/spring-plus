package org.example.expert.domain.manager.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ManagerSaveRequest {

    @NotNull
    private Long managerUserId; // 일정 작상자가 배치하는 유저 id

    public ManagerSaveRequest(Long managerUserId) {
        this.managerUserId = managerUserId;
    }

    public ManagerSaveRequest() {
    }

    public @NotNull Long getManagerUserId() {
        return managerUserId;
    }
}
