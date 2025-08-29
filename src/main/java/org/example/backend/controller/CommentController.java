package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.dto.comment.CommentResponse;
import org.example.backend.dto.comment.CreateCommentRequest;
import org.example.backend.dto.comment.GetCommentsRequest;
import org.example.backend.dto.comment.UpdateCommentRequest;
import org.example.backend.service.comment.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.Set;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateCommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{taskId}/{planId}")
    public ResponseEntity<Set<CommentResponse>> getAllComment(
            @Valid GetCommentsRequest request, Pageable pageable
    ) {
        return ResponseEntity.ok(new LinkedHashSet<>(commentService.getAllComments(request, pageable)));
    }
}
