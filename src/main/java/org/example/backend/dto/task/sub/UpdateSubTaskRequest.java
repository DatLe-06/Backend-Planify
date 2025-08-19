package org.example.backend.dto.task.sub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSubTaskRequest {
    @NotBlank(message = "{subtask.title.invalid}")
    private String title;
    private boolean completed;
    @NotNull(message = "{subtask.task.invalid}")
    private Long taskId;
}
