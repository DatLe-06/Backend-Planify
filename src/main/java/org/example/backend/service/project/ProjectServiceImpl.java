package org.example.backend.service.project;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.backend.dto.project.AddProjectRequest;
import org.example.backend.dto.project.BaseProject;
import org.example.backend.dto.project.ProjectResponse;
import org.example.backend.dto.project.UpdateProjectRequest;
import org.example.backend.entity.Action;
import org.example.backend.entity.History;
import org.example.backend.entity.Project;
import org.example.backend.entity.Tag;
import org.example.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;
    private final PriorityRepository priorityRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    @Transactional
    @Override
    public ProjectResponse create(AddProjectRequest request) {
        if (projectRepository.existsByName(request.getName())) {
            throw new RuntimeException("Project name already exists");
        }

        History history = new History();
        history.setAction(Action.Project.CREATE);
        history.setChangedAt(LocalDateTime.now());

        Project project = mapToEntity(request);
        project.setCreateAt(LocalDateTime.now());
        project.setUpdateAt(LocalDateTime.now());

        history.setProject(project);
        history.setChangedBy(project.getOwner());

        Project data = projectRepository.save(project);
        historyRepository.save(history);
        return mapToDto(data);
    }

    @Override
    public ProjectResponse update(Long id, UpdateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setImageUrl(request.getImageUrl());
        project.setUpdateAt(LocalDateTime.now());

        validateAndLoad(request, project);

        return mapToDto(projectRepository.save(project));
    }

    @Override
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    @Override
    public ProjectResponse getById(Long id) {
        return projectRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public List<ProjectResponse> getAll() {
        return projectRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Project mapToEntity(AddProjectRequest dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setImageUrl(dto.getImageUrl());

        validateAndLoad(dto, project);

        return project;
    }

    private void validateAndLoad(BaseProject dto, Project project) {
        project.setPriority(priorityRepository.findById(dto.getPriorityId())
                .orElseThrow(() -> new RuntimeException("Priority not found")));

        if (dto.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(dto.getTagIds()));
            project.setTags(tags);
        }

        if (dto instanceof AddProjectRequest) {
            project.setOwner(userRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found")));
        }
    }

    private ProjectResponse mapToDto(Project project) {
        ProjectResponse dto = new ProjectResponse();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setImageUrl(project.getImageUrl());
        dto.setCreatedAt(project.getCreateAt());
        dto.setUpdateAt(project.getUpdateAt());

        if (project.getPriority() != null) dto.setPriorityId(project.getPriority().getId());
        if (project.getTags() != null) {
            dto.setTagIds(project.getTags().stream().map(Tag::getId).collect(Collectors.toSet()));
        }
        if (project.getOwner() != null) dto.setOwnerId(project.getOwner().getId());
        return dto;
    }
}
