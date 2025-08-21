package org.example.backend.dto.priority;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePriorityRequest {
    @Size(min = 2, max = 20, message = "{priority.description.invalid.size}")
    @NotNull(message = "{priority.name.invalid}")
    private String name;

    @Size(min = 2, max = 200, message = "{priority.description.invalid.size}")
    private String description;

    @NotNull(message = "{priority.type.invalid}")
    @Pattern(regexp = "TASK|PLAN")
    private String type;
}
