package org.example.backend.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTagRequest {
    @Size(min = 2, max = 20, message = "{tag.description.invalid.size}")
    @NotBlank(message = "{tag.name.invalid}")
    private String name;

    @Size(min = 2, max = 200, message = "{tag.description.invalid.size}")
    private String description;

    @NotBlank(message = "{tag.type.invalid}")
    @Pattern(regexp = "PLAN|TASK")
    private String type;
}
