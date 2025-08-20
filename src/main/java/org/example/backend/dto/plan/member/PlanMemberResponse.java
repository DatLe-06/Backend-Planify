package org.example.backend.dto.plan.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.backend.dto.user.ProfileResponse;
import org.example.backend.entity.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanMemberResponse {
    private Long id;
    private Long projectId;
    private ProfileResponse member;
    private String role;
}
