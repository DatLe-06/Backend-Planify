package org.example.backend.mapper;

import org.example.backend.constant.Type;
import org.example.backend.dto.priority.CreatePriorityRequest;
import org.example.backend.dto.priority.PriorityResponse;
import org.example.backend.dto.priority.UpdatePriorityRequest;
import org.example.backend.entity.Priority;
import org.example.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PriorityMapper {
    public PriorityResponse toResponse(Priority priority) {
        PriorityResponse response = new PriorityResponse();
        response.setId(priority.getId());
        response.setName(priority.getName());
        if (priority.getDescription() != null) response.setDescription(priority.getDescription());
        response.setOrderPriority(priority.getOrderPriority());
        return response;
    }

    public Priority toEntityForCreate(CreatePriorityRequest request, User createdBy, int maxOrder) {
        Priority priority = new Priority();
        priority.setName(request.getName());
        if (request.getDescription() != null) priority.setDescription(request.getDescription());
        priority.setType(Type.valueOf(request.getType()));
        priority.setCreatedBy(createdBy);
        priority.setOrderPriority(maxOrder + 1);
        return priority;
    }

    public void toEntityForUpdate(Priority priority, UpdatePriorityRequest request) {
        priority.setName(request.getName());
        if (request.getDescription() != null) priority.setDescription(request.getDescription());
        priority.setOrderPriority(request.getOrder());
    }
}
