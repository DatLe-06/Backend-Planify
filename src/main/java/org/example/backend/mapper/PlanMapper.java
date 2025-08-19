package org.example.backend.mapper;

import org.example.backend.dto.plan.AddPlanRequest;
import org.example.backend.dto.plan.PlanRequest;
import org.example.backend.dto.plan.PlanResponse;
import org.example.backend.entity.Plan;
import org.example.backend.entity.Priority;
import org.example.backend.entity.Tag;
import org.example.backend.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PlanMapper {

    public PlanResponse toResponse(Plan plan, String coverUrl) {
        PlanResponse dto = new PlanResponse();
        dto.setId(plan.getId());
        dto.setName(plan.getTitle());
        if (plan.getDescription() != null) dto.setDescription(plan.getDescription());
        if (plan.getCoverPublicId() != null) dto.setCoverUrl(coverUrl);
        dto.setCreatedAt(plan.getCreatedAt());
        dto.setUpdateAt(plan.getUpdatedAt());

        if (plan.getPriority() != null) dto.setPriorityId(plan.getPriority().getId());
        if (plan.getTags() != null) {
            dto.setTagIds(plan.getTags().stream().map(Tag::getId).collect(Collectors.toSet()));
        }
        if (plan.getOwner() != null) dto.setOwnerId(plan.getOwner().getId());
        return dto;
    }

    public void toEntity(PlanRequest request, Priority priority, Set<Tag> tags, String coverPublicId, User user, Plan plan) {
        plan.setTitle(request.getTitle());
        plan.setDescription(request.getDescription());
        if (priority != null) plan.setPriority(priority);
        if (tags != null) plan.setTags(tags);
        if (coverPublicId != null) plan.setCoverPublicId(coverPublicId);
        plan.setUpdatedAt(LocalDateTime.now());
        if (request instanceof AddPlanRequest) {
            plan.setOwner(user);
            plan.setCreatedAt(LocalDateTime.now());
        }
    }
}
