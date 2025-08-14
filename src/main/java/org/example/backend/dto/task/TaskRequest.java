package org.example.backend.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public abstract class TaskRequest {
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer statusId;
    private Integer priorityId;
    private Set<Long> memberIds;
    private Long projectId;
    private Set<Long> tagIds;
}
