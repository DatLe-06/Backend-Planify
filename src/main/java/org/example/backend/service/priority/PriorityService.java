package org.example.backend.service.priority;

import org.example.backend.constant.Direction;
import org.example.backend.constant.Type;
import org.example.backend.dto.priority.CreatePriorityRequest;
import org.example.backend.dto.priority.PriorityResponse;
import org.example.backend.dto.priority.UpdatePriorityRequest;
import org.example.backend.entity.Priority;

import java.util.Set;

public interface PriorityService {
    Priority findById(Integer id);
    PriorityResponse create(CreatePriorityRequest request);
    PriorityResponse update(Integer id, UpdatePriorityRequest request);
    void delete(Integer id);
    Set<PriorityResponse> getAllPriorities(Type type, Direction direction);
}
