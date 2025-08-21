package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.constant.Direction;
import org.example.backend.constant.Type;
import org.example.backend.dto.priority.CreatePriorityRequest;
import org.example.backend.dto.priority.UpdatePriorityRequest;
import org.example.backend.service.priority.PriorityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/priorities")
@AllArgsConstructor
public class PriorityController {
    private PriorityService priorityService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreatePriorityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(priorityService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody UpdatePriorityRequest request) {
        return ResponseEntity.ok(priorityService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        priorityService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("delete.priority.success");
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam Type type,
                                    @RequestParam(defaultValue = "ASC") Direction direction) {
        return ResponseEntity.ok(priorityService.getAllPriorities(type, direction));
    }
}
