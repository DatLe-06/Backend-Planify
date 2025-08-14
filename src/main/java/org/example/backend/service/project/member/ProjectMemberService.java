package org.example.backend.service.project.member;

import org.example.backend.dto.project.member.AddProjectMemberRequest;
import org.example.backend.dto.project.member.ProjectMemberResponse;

import java.util.List;

public interface ProjectMemberService {
    ProjectMemberResponse addProjectMember(AddProjectMemberRequest request);
    ProjectMemberResponse updateProjectMember(Long id, AddProjectMemberRequest request);
    void deleteProjectMember(Long id);
    ProjectMemberResponse getProjectMemberById(Long id);
    List<ProjectMemberResponse> getAllMembers();
}

