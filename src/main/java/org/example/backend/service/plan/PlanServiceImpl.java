package org.example.backend.service.plan;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.backend.dto.plan.AddPlanRequest;
import org.example.backend.dto.plan.BasePlan;
import org.example.backend.dto.plan.PlanResponse;
import org.example.backend.dto.plan.UpdatePlanRequest;
import org.example.backend.entity.*;
import org.example.backend.exception.custom.*;
import org.example.backend.repository.*;
import org.example.backend.service.history.HistoryService;
import org.example.backend.utils.MessageUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final PriorityRepository priorityRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final MessageUtils messageUtils;
    private final HistoryService historyService;

    @Transactional
    @Override
    public PlanResponse create(AddPlanRequest request) {
        if (planRepository.existsByName(request.getName())) {
            throw new PlanNameDuplicateException(messageUtils.getMessage("plan.name.duplicate", request.getName()));
        }
        Plan plan = new Plan();

        validateAndLoad(request, plan);
        plan.setCreatedAt(LocalDateTime.now());

        planRepository.save(plan);
        historyService.createHistory(Type.PLAN, plan.getId(), plan.getName(), Action.Plan.CREATE, plan.getOwner());
        return mapToDto(plan);
    }

    @Transactional
    @Override
    public PlanResponse update(Long id, UpdatePlanRequest request) {
        if (planRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new PlanNameDuplicateException(messageUtils.getMessage("plan.name.duplicate", request.getName()));
        }
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException(messageUtils.getMessage("plan.not.found")));

        validateAndLoad(request, plan);

        planRepository.save(plan);
        historyService.createHistory(Type.PLAN, plan.getId(), plan.getName(), Action.Plan.UPDATE, plan.getOwner());
        return mapToDto(plan);
    }

    @Transactional
    @Override
    public String delete(Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new PlanNotFoundException(messageUtils.getMessage("plan.not.found")));
        History history = historyService.createHistory(Type.PLAN, plan.getId(), plan.getName(), Action.Plan.DELETE, plan.getOwner());
        planRepository.deleteById(id);
        return messageUtils.getMessage("delete.plan.success", history.getName());
    }

    @Override
    public PlanResponse getById(Long id) {
        return planRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new PlanNotFoundException(messageUtils.getMessage("plan.not.found")));
    }

    @Override
    public List<PlanResponse> getAll() {
        return planRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private void validateAndLoad(BasePlan request, Plan plan) {
        plan.setName(request.getName());
        plan.setUpdatedAt(LocalDateTime.now());
        if (request.getImageUrl() != null) plan.setImageUrl(request.getImageUrl());
        if (request.getDescription() != null) plan.setDescription(request.getDescription());
        Priority priority = priorityRepository.findByIdAndType(request.getPriorityId(), Type.PLAN);
        if (priority == null) throw new PriorityNotFoundException(messageUtils.getMessage("priority.not.found"));
        plan.setPriority(priority);
        if (request.getTagIds() != null) {
            Set<Tag> tags = request.getTagIds().stream().map(tagId -> tagRepository.findById(tagId)
                    .orElseThrow(() -> new TagNotFoundException(messageUtils.getMessage("tag.not.found")))).collect(Collectors.toSet());
            plan.setTags(tags);
        }
        User user = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException(messageUtils.getMessage("user.not.found")));
        plan.setOwner(user);
    }

    private PlanResponse mapToDto(Plan plan) {
        PlanResponse dto = new PlanResponse();
        dto.setId(plan.getId());
        dto.setName(plan.getName());
        if (plan.getDescription() != null) dto.setDescription(plan.getDescription());
        if (plan.getImageUrl() != null) dto.setImageUrl(plan.getImageUrl());
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
