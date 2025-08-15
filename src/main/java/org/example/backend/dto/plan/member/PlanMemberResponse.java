package org.example.backend.dto.plan.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanMemberResponse {
    private Long id;
    private Long projectId;
    private Long userId;
    private String role;
}
