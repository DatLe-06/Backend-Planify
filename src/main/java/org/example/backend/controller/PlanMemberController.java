package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.project.member.AddPlanMemberRequest;
import org.example.backend.dto.project.member.PlanMemberResponse;
import org.example.backend.service.plan.member.PlanMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plan-members")
@RequiredArgsConstructor
public class PlanMemberController {
    private final PlanMemberService planMemberService;

    @PostMapping
    public ResponseEntity<PlanMemberResponse> addPlanMember(@RequestBody AddPlanMemberRequest request) {
        return ResponseEntity.ok(planMemberService.addPlanMember(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanMemberResponse> updatePlanMember(
            @PathVariable Long id,
            @RequestBody AddPlanMemberRequest request) {
        return ResponseEntity.ok(planMemberService.updatePlanMember(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanMember(@PathVariable Long id) {
        planMemberService.deletePlanMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanMemberResponse> getPlanMember(@PathVariable Long id) {
        return ResponseEntity.ok(planMemberService.getPlanMemberById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlanMemberResponse>> getAllPlanMembers() {
        return ResponseEntity.ok(planMemberService.getAllMembers());
    }
}

