package org.example.backend.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class TaskResponse {
    private Long id;
    private String content;
    private Double progress;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private String statusName;
    private String priorityName;
    private Set<String> memberNames;
    private String projectName;
    private Set<String> tagNames;
}