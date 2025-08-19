package org.example.backend.dto.plan;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public abstract class PlanRequest {
    @Size(min = 2, max = 100, message = "{plan.name.invalid.size}")
    @NotNull(message = "{plan.name.invalid}")
    private String title;

    @Size(min = 2, max = 255, message = "{plan.description.invalid.size}")
    @NotNull(message = "{plan.description.not.blank}")
    private String description;

    private MultipartFile coverFile;

    @NotNull(message = "{plan.priority.invalid}")
    private Integer priorityId;

    @Size(min = 1, message = "{tags.invalid}")
    private Set<@NotNull Long> tagIds;
}
