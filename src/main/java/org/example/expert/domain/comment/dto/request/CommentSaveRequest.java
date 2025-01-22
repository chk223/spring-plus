package org.example.expert.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CommentSaveRequest {

    @NotBlank
    private String contents;

    public CommentSaveRequest() {
    }

    public CommentSaveRequest(String contents) {
        this.contents = contents;
    }

    public @NotBlank String getContents() {
        return contents;
    }
}
