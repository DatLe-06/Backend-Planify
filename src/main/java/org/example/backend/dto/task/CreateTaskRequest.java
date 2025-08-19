package org.example.backend.dto.task;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class CreateTaskRequest {
    @Size(min = 2, max = 100, message = "{task.title.invalid.size}")
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
    private Set<@NotNull @Positive Long> memberIds;

    @NotNull(message = "{plan.not.null}")
    private Long planId;

    @Size(min = 1, message = "{tags.invalid}")
    private Set<@NotNull @Positive Long> tagIds;

    @AssertTrue(message = "{end.date.must.be.after.start.date}")
    public boolean isEndDateAfterStartDate() {
        return startDate != null && endDate != null && endDate.isAfter(startDate);
    }
}


