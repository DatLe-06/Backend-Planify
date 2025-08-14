package org.example.backend.service.plan;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.backend.dto.project.AddProjectRequest;
import org.example.backend.dto.project.BaseProject;
import org.example.backend.dto.project.ProjectResponse;
import org.example.backend.dto.project.UpdateProjectRequest;
import org.example.backend.entity.Action;
import org.example.backend.entity.History;
import org.example.backend.entity.Plan;
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
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final PriorityRepository priorityRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    @Transactional
    @Override
    public ProjectResponse create(AddProjectRequest request) {
        if (planRepository.existsByName(request.getName())) {
            throw new RuntimeException("Project name already exists");
        }

        History history = new History();
        history.setAction(Action.Project.CREATE);
        history.setChangedAt(LocalDateTime.now());

        Plan plan = mapToEntity(request);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());

        history.setPlan(plan);
        history.setChangedBy(plan.getOwner());

        Plan data = planRepository.save(plan);
        historyRepository.save(history);
        return mapToDto(data);
    }

    @Override
    public ProjectResponse update(Long id, UpdateProjectRequest request) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setImageUrl(request.getImageUrl());
        plan.setUpdatedAt(LocalDateTime.now());

        validateAndLoad(request, plan);

        return mapToDto(planRepository.save(plan));
    }

    @Override
    public void delete(Long id) {
        if (!planRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }
        planRepository.deleteById(id);
    }

    @Override
    public ProjectResponse getById(Long id) {
        return planRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public List<ProjectResponse> getAll() {
        return planRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Plan mapToEntity(AddProjectRequest dto) {
        Plan plan = new Plan();
        plan.setName(dto.getName());
        plan.setDescription(dto.getDescription());
        plan.setImageUrl(dto.getImageUrl());

        validateAndLoad(dto, plan);

        return plan;
    }

    private void validateAndLoad(BaseProject dto, Plan plan) {
        plan.setPriority(priorityRepository.findById(dto.getPriorityId())
                .orElseThrow(() -> new RuntimeException("Priority not found")));

        if (dto.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(dto.getTagIds()));
            plan.setTags(tags);
        }

        if (dto instanceof AddProjectRequest) {
            plan.setOwner(userRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found")));
        }
    }

    private ProjectResponse mapToDto(Plan plan) {
        ProjectResponse dto = new ProjectResponse();
        dto.setId(plan.getId());
        dto.setName(plan.getName());
        dto.setDescription(plan.getDescription());
        dto.setImageUrl(plan.getImageUrl());
        dto.setCreatedAt(plan.getCreatedAt());
        dto.setUpdateAt(plan.getUpdatedAt());

        if (plan.getPriority() != null) dto.setPriorityId(plan.getPriority().getId());
        if (plan.getTags() != null) {
            dto.setTagIds(plan.getTags().stream().map(Tag::getId).collect(Collectors.toSet()));
        }
        if (plan.getOwner() != null) dto.setOwnerId(plan.getOwner().getId());
        return dto;
    }
}
