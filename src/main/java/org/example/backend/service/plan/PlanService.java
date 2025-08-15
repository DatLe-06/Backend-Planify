package org.example.backend.service.plan;

import org.example.backend.dto.plan.AddPlanRequest;
import org.example.backend.dto.plan.PlanResponse;
import org.example.backend.dto.plan.UpdatePlanRequest;

import java.util.List;

public interface PlanService {
    PlanResponse create(AddPlanRequest request);
    PlanResponse update(Long id, UpdatePlanRequest request);
    String delete(Long id);
    PlanResponse getById(Long id);
    List<PlanResponse> getAll();
}
