package org.example.backend.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public abstract class BaseProject {
    @Size(min = 2, max = 100, message = "Project name must be between 2 and 100 characters")
    @NotNull(message = "Project name cannot be null")
    private String name;

    @Size(min = 2, max = 255, message = "Project description must be between 2 and 255 characters")
    @NotBlank(message = "Project description cannot be blank")
    private String description;

    @NotNull(message = "Project priority cannot be null")
    private String imageUrl;

    @NotNull(message = "Project Priority cannot be null")
    private Integer priorityId;

    private Set<@NotNull Long> tagIds;

    @NotNull(message = "Project owner cannot be null")
    private Long ownerId;
}
