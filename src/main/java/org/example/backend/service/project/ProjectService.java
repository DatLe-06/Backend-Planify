package org.example.backend.service.project;

import org.example.backend.dto.project.AddProjectRequest;
import org.example.backend.dto.project.ProjectResponse;
import org.example.backend.dto.project.UpdateProjectRequest;

import java.util.List;

public interface ProjectService {
    ProjectResponse create(AddProjectRequest request);
    ProjectResponse update(Long id, UpdateProjectRequest request);
    void delete(Long id);
    ProjectResponse getById(Long id);
    List<ProjectResponse> getAll();
}
