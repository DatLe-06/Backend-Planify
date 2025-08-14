package org.example.backend.service.plan.member;

import org.example.backend.dto.project.member.AddPlanMemberRequest;
import org.example.backend.dto.project.member.PlanMemberResponse;

import java.util.List;

public interface PlanMemberService {
    PlanMemberResponse addPlanMember(AddPlanMemberRequest request);
    PlanMemberResponse updatePlanMember(Long id, AddPlanMemberRequest request);
    void deletePlanMember(Long id);
    PlanMemberResponse getPlanMemberById(Long id);
    List<PlanMemberResponse> getAllMembers();
}

