package org.example.backend.dto.task.sub;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubTaskResponse {
    private Long id;
    private String title;
    private boolean completed;
    private Long taskId;
}