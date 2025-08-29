package org.example.backend.dto.task;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public abstract class TaskRequest {
    @NotNull(message = "{task.title.not.null}")
    @Size(min = 2, max = 100, message = "{task.title.invalid.size}")
    private String title;

    @Size(min = 2, max = 255, message = "{task.description.invalid.size}")
    private String description;

    @NotNull(message = "{task.start-date.invalid}")
    @Future(message = "{start.date.must.be.future}")
    private LocalDateTime startDate;

    @NotNull(message = "{task.end-date.invalid}")
    @Future(message = "{end.date.must.be.future}")
    private LocalDateTime endDate;

    @NotNull(message = "{task.status.invalid}")
    private Integer statusId;

    @NotNull(message = "{task.priority.invalid}")
    private Integer priorityId;

    @Size(min = 1, message = "{members.invalid}")
    private Set<@NotNull @Positive Long> memberIds;

    @Size(min = 1, message = "{tags.invalid}")
    private Set<@NotNull @Positive Long> tagIds;

    @AssertTrue(message = "{end.date.must.be.after.start.date}")
    public boolean isEndDateAfterStartDate() {
        return endDate.isAfter(startDate);
    }
}
