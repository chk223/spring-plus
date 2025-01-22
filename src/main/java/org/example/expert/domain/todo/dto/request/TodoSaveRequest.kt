package org.example.expert.domain.todo.dto.request

import jakarta.validation.constraints.NotBlank
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor

class TodoSaveRequest {
    val title: @NotBlank String? = null
    val contents: @NotBlank String? = null
}
