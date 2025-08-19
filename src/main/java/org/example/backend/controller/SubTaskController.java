package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.task.sub.CreateSubTaskRequest;
import org.example.backend.dto.task.sub.SubTaskResponse;
import org.example.backend.dto.task.sub.UpdateSubTaskRequest;
import org.example.backend.service.task.sub.SubTaskService;
import org.example.backend.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sub-tasks")
@RequiredArgsConstructor
public class SubTaskController {
    private final SubTaskService subTaskService;
    private final MessageUtils messageUtils;

    @PostMapping
    public ResponseEntity<SubTaskResponse> createSubTask(@RequestBody CreateSubTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subTaskService.createSubTask(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<SubTaskResponse>> getSubTasksByTaskId(@PathVariable Long id) {
        return ResponseEntity.ok(subTaskService.getAllSubTasksByTaskId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubTaskResponse> updateSubTask(@PathVariable Long id, @RequestBody UpdateSubTaskRequest request) {
        return ResponseEntity.ok(subTaskService.updateSubTask(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubTask(@PathVariable Long id) {
        subTaskService.deleteSubTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageUtils.getMessage("delete.subtask.success"));
    }
}
