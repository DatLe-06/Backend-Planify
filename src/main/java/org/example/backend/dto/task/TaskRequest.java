package org.example.backend.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public abstract class TaskRequest {
    @NotNull(message = "{task.title.invalid}")
    @NotBlank(message = "{task.title.not.blank}")
    private String title;
    
    @Size(min = 2, max = 255, message = "{task.description.invalid.size}")
    private String description;
    
    @NotNull(message = "{start.date.invalid}")
    @Future(message = "{start.date.must.be.future}")
    private LocalDateTime startDate;

    @NotNull(message = "{end.date.invalid}")
    @Future(message = "{end.date.must.be.future}")
    private LocalDateTime endDate;

    @NotNull(message = "{status.not.null}")
    private Integer statusId;

    @NotNull(message = "{priority.not.null}")
    private Integer priorityId;

    @Size(min = 1, message = "{members.invalid}")
    private Set<@NotNull Long> memberIds;

    @NotNull(message = "{plan.not.null}")
    private Long planId;

    @Size(min = 1, message = "{tags.invalid}")
    private Set<@NotNull Long> tagIds;
}
