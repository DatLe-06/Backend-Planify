package org.example.backend.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskRequest extends TaskRequest {
    @NotNull(message = "{plan.not.null}")
    private Long planId;
}


