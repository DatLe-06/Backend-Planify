package org.example.backend.service.plan;

import org.example.backend.dto.plan.AddPlanRequest;
import org.example.backend.dto.plan.PlanResponse;
import org.example.backend.dto.plan.UpdatePlanRequest;
import org.example.backend.entity.Plan;

import java.util.List;

public interface PlanService {
    PlanResponse create(AddPlanRequest request);
    PlanResponse update(Long id, UpdatePlanRequest request);
    void softDelete(Long id);
    void hardDelete(Long id);
    PlanResponse restore(Long id);
    PlanResponse getById(Long id);
    List<PlanResponse> getAll();
    Plan findById(Long id);
}
