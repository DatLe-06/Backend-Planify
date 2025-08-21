package org.example.backend.dto.priority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriorityResponse {
    private Integer id;
    private String name;
    private String description;
    private Integer orderPriority;
}
