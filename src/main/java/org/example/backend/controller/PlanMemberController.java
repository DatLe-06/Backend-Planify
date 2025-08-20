package org.example.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.backend.constant.PlanRole;
import org.example.backend.dto.plan.member.CreatePlanMemberRequest;
import org.example.backend.dto.plan.member.PlanMemberResponse;
import org.example.backend.dto.plan.member.UpdatePlanMemberRequest;
import org.example.backend.dto.user.ProfileResponse;
import org.example.backend.entity.Plan;
import org.example.backend.entity.User;
import org.example.backend.service.plan.member.PlanMemberService;
import org.example.backend.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/plan-members")
@RequiredArgsConstructor
public class PlanMemberController {
    private final PlanMemberService planMemberService;
    private final MessageUtils messageUtils;

    @PostMapping
    public ResponseEntity<PlanMemberResponse> addMember(@Valid @RequestBody CreatePlanMemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planMemberService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanMemberResponse> updateMember(
            @PathVariable Long id,
            @RequestBody UpdatePlanMemberRequest request) {
        return ResponseEntity.ok(planMemberService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeMember(@PathVariable Long id) {
        planMemberService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageUtils.getMessage("remove.member.success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<PlanMemberResponse>> getMembersInPlan(@PathVariable Long id) {
        return ResponseEntity.ok(planMemberService.getMembersInPlan(id));
    }
}

