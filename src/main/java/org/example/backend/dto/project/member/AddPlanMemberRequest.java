package org.example.backend.dto.project.member;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.entity.PlanRole;

@Getter
@Setter
public class AddPlanMemberRequest {
    @NotNull(message = "Plan ID cannot be null")
    private Long planId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Role cannot be null")
    private PlanRole role;
}