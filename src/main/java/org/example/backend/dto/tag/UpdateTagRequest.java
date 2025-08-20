package org.example.backend.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTagRequest {
    @Size(min = 2, max = 20, message = "{tag.description.invalid.size}")
    @NotBlank(message = "{tag.name.invalid}")
    private String name;

    @Size(min = 2, max = 200, message = "{tag.description.invalid.size}")
    private String description;
}
