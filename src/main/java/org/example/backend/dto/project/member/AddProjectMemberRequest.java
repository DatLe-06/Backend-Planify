package org.example.backend.dto.project.member;

import lombok.Getter;
import lombok.Setter;
import org.example.backend.entity.ProjectRole;

@Getter
@Setter
public class AddProjectMemberRequest {
    private Long projectId;
    private Long userId;
    private ProjectRole role;
}