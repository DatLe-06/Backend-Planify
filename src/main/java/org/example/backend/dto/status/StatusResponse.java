package org.example.backend.dto.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse {
    private Integer id;
    private String name;
    private String description;
    private Long createdById;
    private String createdByUsername;
}

