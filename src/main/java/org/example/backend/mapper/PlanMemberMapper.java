package org.example.backend.mapper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.example.backend.constant.PlanRole;
import org.example.backend.dto.plan.member.PlanMemberResponse;
import org.example.backend.dto.user.ProfileResponse;
import org.example.backend.entity.Plan;
import org.example.backend.entity.PlanMember;
import org.example.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PlanMemberMapper {
    public PlanMemberResponse toResponse(
            PlanMember planMember, Long planId, ProfileResponse profile, String role) {
        PlanMemberResponse response = new PlanMemberResponse();
        response.setId(planMember.getId());
        response.setMember(profile);
        response.setRole(role);
        response.setProjectId(planId);
        return response;
    }

    public PlanMember toEntityForCreate(Plan plan, User member, PlanRole planRole) {
        PlanMember planMember = new PlanMember();
        planMember.setPlan(plan);
        planMember.setMember(member);
        planMember.setRole(planRole);
        return planMember;
    }

    public void toEntityForUpdate(PlanMember planMember, String role) {
        planMember.setRole(PlanRole.valueOf(role));
    }
}
