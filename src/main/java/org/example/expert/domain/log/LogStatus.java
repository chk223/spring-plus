package org.example.expert.domain.log;

import lombok.Getter;

@Getter
public enum LogStatus {
    SUCCESS("success"),
    FAILED("failed");

    private final String state;

    LogStatus(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
