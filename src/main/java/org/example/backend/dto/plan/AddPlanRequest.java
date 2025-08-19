package org.example.backend.dto.plan;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPlanRequest extends PlanRequest{
    @NotNull(message = "{plan.owner.invalid}")
    private Long ownerId;
}
