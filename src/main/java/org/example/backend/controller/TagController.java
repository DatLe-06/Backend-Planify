package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.constant.Type;
import org.example.backend.dto.tag.CreateTagRequest;
import org.example.backend.dto.tag.TagResponse;
import org.example.backend.dto.tag.UpdateTagRequest;
import org.example.backend.service.tag.TagService;
import org.example.backend.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class TagController {
    private final MessageUtils messageUtils;
    private TagService tagService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateTagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateTagRequest request) {
        return ResponseEntity.ok(tagService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageUtils.getMessage("delete.tag.success"));
    }

    @GetMapping(value = "/task-tag" )
    public ResponseEntity<Set<TagResponse>> getAllForTask() {
        return ResponseEntity.ok(tagService.getAllTags(Type.TASK));
    }

    @GetMapping(value = "/plan-tag")
    public ResponseEntity<Set<TagResponse>> getAllForPlan() {
        return ResponseEntity.ok(tagService.getAllTags(Type.PLAN));
    }
}
