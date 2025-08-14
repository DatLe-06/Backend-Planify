package org.example.backend.dto.task.sub;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSubTaskRequest {
    private String title;
    private boolean completed;
    private Long taskId;
}
