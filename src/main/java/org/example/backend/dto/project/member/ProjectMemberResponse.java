package org.example.backend.dto.project.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberResponse {
    private Long id;
    private Long projectId;
    private Long userId;
    private String role;
}
