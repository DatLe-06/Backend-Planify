package org.example.backend.dto.plan;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

@Getter
@Setter
public abstract class BasePlan {
    @Size(min = 2, max = 100, message = "{plan.name.invalid.size}")
    @NotNull(message = "{plan.name.invalid}")
    private String title;

    @Size(min = 2, max = 255, message = "{plan.description.invalid.size}")
    @NotBlank(message = "{plan.description.not.blank}")
    private String description;

    @URL(message = "{url.invalid}")
    private String imageUrl;

    @NotNull(message = "{plan.priority.invalid}")
    private Integer priorityId;

    @Size(min = 1, message = "{tags.invalid}")
    private Set<@NotNull Long> tagIds;

    @NotNull(message = "{plan.owner.invalid}")
    private Long ownerId;
}
