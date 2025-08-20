package org.example.backend.service.plan.member;

import org.example.backend.constant.PlanRole;
import org.example.backend.dto.plan.member.CreatePlanMemberRequest;
import org.example.backend.dto.plan.member.PlanMemberResponse;
import org.example.backend.dto.plan.member.UpdatePlanMemberRequest;
import org.example.backend.dto.user.ProfileResponse;
import org.example.backend.entity.Plan;
import org.example.backend.entity.User;

import java.util.Collection;
import java.util.Set;

public interface PlanMemberService {
    PlanMemberResponse create(CreatePlanMemberRequest request);
    PlanMemberResponse update(Long id, UpdatePlanMemberRequest request);
    void delete(Long id);
    Set<PlanMemberResponse> getMembersInPlan(Long id);
}
