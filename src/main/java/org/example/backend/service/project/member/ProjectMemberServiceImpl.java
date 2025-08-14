package org.example.backend.service.project.member;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.project.member.AddProjectMemberRequest;
import org.example.backend.dto.project.member.ProjectMemberResponse;
import org.example.backend.entity.ProjectMember;
import org.example.backend.repository.ProjectMemberRepository;
import org.example.backend.repository.ProjectRepository;
import org.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectMemberResponse addProjectMember(AddProjectMemberRequest request) {
        ProjectMember member = new ProjectMember();
        return getProjectMemberResponse(request, member);
    }

    @NotNull
    private ProjectMemberResponse getProjectMemberResponse(AddProjectMemberRequest request, ProjectMember member) {
        member.setProject(projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found")));
        member.setMember(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        member.setRole(request.getRole());

        ProjectMember saved = projectMemberRepository.save(member);
        return toResponse(saved);
    }

    @Override
    public ProjectMemberResponse updateProjectMember(Long id, AddProjectMemberRequest request) {
        ProjectMember member = projectMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjectMember not found"));

        return getProjectMemberResponse(request, member);
    }

    @Override
    public void deleteProjectMember(Long id) {
        projectMemberRepository.deleteById(id);
    }

    @Override
    public ProjectMemberResponse getProjectMemberById(Long id) {
        ProjectMember member = projectMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjectMember not found"));
        return toResponse(member);
    }

    @Override
    public List<ProjectMemberResponse> getAllMembers() {
        return projectMemberRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ProjectMemberResponse toResponse(ProjectMember member) {
        return new ProjectMemberResponse(
                member.getId(),
                member.getProject().getId(),
                member.getMember().getId(),
                member.getRole().name()
        );
    }
}

