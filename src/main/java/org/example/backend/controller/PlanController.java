package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.plan.AddPlanRequest;
import org.example.backend.dto.plan.PlanResponse;
import org.example.backend.dto.plan.UpdatePlanRequest;
import org.example.backend.service.plan.PlanServiceImpl;
import org.example.backend.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanServiceImpl planService;
    private final MessageUtils messageUtils;

    @PostMapping
    public ResponseEntity<?> create(@Valid @ModelAttribute AddPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanResponse> update(@PathVariable Long id, @Valid @ModelAttribute UpdatePlanRequest request) {
        return ResponseEntity.ok(planService.update(id, request));
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable Long id) {
        return ResponseEntity.ok(planService.restore(id));
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<?> softDelete(@PathVariable Long id) {
        planService.softDelete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageUtils.getMessage("delete.plan.success"));
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<?> hardDelete(@PathVariable Long id) {
        planService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(planService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlanResponse>> getAll() {
        return ResponseEntity.ok(planService.getAll());
    }
}
