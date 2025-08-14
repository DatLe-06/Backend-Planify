package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.project.member.AddProjectMemberRequest;
import org.example.backend.dto.project.member.ProjectMemberResponse;
import org.example.backend.service.project.member.ProjectMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-members")
@RequiredArgsConstructor
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<ProjectMemberResponse> addProjectMember(@RequestBody AddProjectMemberRequest request) {
        return ResponseEntity.ok(projectMemberService.addProjectMember(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectMemberResponse> updateProjectMember(
            @PathVariable Long id,
            @RequestBody AddProjectMemberRequest request) {
        return ResponseEntity.ok(projectMemberService.updateProjectMember(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjectMember(@PathVariable Long id) {
        projectMemberService.deleteProjectMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectMemberResponse> getProjectMember(@PathVariable Long id) {
        return ResponseEntity.ok(projectMemberService.getProjectMemberById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getAllProjectMembers() {
        return ResponseEntity.ok(projectMemberService.getAllMembers());
    }
}

