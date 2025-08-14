package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.task.sub.AddSubTaskRequest;
import org.example.backend.dto.task.sub.SubTaskResponse;
import org.example.backend.service.task.sub.SubTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sub-tasks")
@RequiredArgsConstructor
public class SubTaskController {
    private final SubTaskService subTaskService;

    @PostMapping
    public ResponseEntity<SubTaskResponse> createSubTask(@RequestBody AddSubTaskRequest request) {
        return ResponseEntity.ok(subTaskService.createSubTask(request));
    }

    @GetMapping
    public ResponseEntity<List<SubTaskResponse>> getAllSubTasks() {
        return ResponseEntity.ok(subTaskService.getAllSubTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubTaskResponse> getSubTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(subTaskService.getSubTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubTaskResponse> updateSubTask(@PathVariable Long id, @RequestBody AddSubTaskRequest request) {
        return ResponseEntity.ok(subTaskService.updateSubTask(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long id) {
        subTaskService.deleteSubTask(id);
        return ResponseEntity.noContent().build();
    }
}
