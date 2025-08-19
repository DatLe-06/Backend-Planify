package org.example.backend.dto.task;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UpdateTaskRequest {
    @Size(min = 2, max = 100, message = "{task.title.invalid.size}")
    private String title;

    @Size(min = 2, max = 255, message = "{task.description.invalid.size}")
    private String description;

    @Future(message = "{start.date.must.be.future}")
    private LocalDateTime startDate;

    @Future(message = "{end.date.must.be.future}")
    private LocalDateTime endDate;

    private Integer statusId;

    private Integer priorityId;

    @Size(min = 1, message = "{members.invalid}")
    private Set<@NotNull @Positive Long> memberIds;

    @Size(min = 1, message = "{tags.invalid}")
    private Set<@NotNull @Positive Long> tagIds;

    @AssertTrue(message = "{end.date.must.be.after.start.date}")
    public boolean isEndDateAfterStartDate() {
        if (startDate == null || endDate == null) return true;
        return endDate.isAfter(startDate);
    }
}


