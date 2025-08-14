package org.example.backend.service.plan.member;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.plan.member.AddPlanMemberRequest;
import org.example.backend.dto.plan.member.PlanMemberResponse;
import org.example.backend.entity.PlanMember;
import org.example.backend.repository.PlanMemberRepository;
import org.example.backend.repository.PlanRepository;
import org.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanMemberServiceImpl implements PlanMemberService {
    private final PlanMemberRepository planMemberRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Override
    public PlanMemberResponse addPlanMember(AddPlanMemberRequest request) {
        PlanMember member = new PlanMember();
        return getPlanMemberResponse(request, member);
    }

    @NotNull
    private PlanMemberResponse getPlanMemberResponse(AddPlanMemberRequest request, PlanMember member) {
        member.setPlan(planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found")));
        member.setMember(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        member.setRole(request.getRole());

        PlanMember saved = planMemberRepository.save(member);
        return toResponse(saved);
    }

    @Override
    public PlanMemberResponse updatePlanMember(Long id, AddPlanMemberRequest request) {
        PlanMember member = planMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjectMember not found"));

        return getPlanMemberResponse(request, member);
    }

    @Override
    public void deletePlanMember(Long id) {
        planMemberRepository.deleteById(id);
    }

    @Override
    public PlanMemberResponse getPlanMemberById(Long id) {
        PlanMember member = planMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjectMember not found"));
        return toResponse(member);
    }

    @Override
    public List<PlanMemberResponse> getAllMembers() {
        return planMemberRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PlanMemberResponse toResponse(PlanMember member) {
        return new PlanMemberResponse(
                member.getId(),
                member.getPlan().getId(),
                member.getMember().getId(),
                member.getRole().name()
        );
    }
}

