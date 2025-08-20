package org.example.backend.dto.plan.member;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class PlanMemberRequest {
    @NotNull(message = "Plan ID cannot be null")
    private Long planId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Role cannot be null")
    @Pattern(regexp = "ALL|EDITOR|VIEWER")
    private String role;
}
