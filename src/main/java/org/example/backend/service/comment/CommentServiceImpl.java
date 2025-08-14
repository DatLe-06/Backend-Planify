package org.example.backend.service.comment;

import org.example.backend.dto.comment.AddCommentRequest;
import org.example.backend.dto.comment.CommentResponse;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.backend.entity.Comment;
import org.example.backend.entity.Plan;
import org.example.backend.entity.Task;
import org.example.backend.entity.User;
import org.example.backend.repository.CommentRepository;
import org.example.backend.repository.PlanRepository;
import org.example.backend.repository.TaskRepository;
import org.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponse createComment(AddCommentRequest request) {
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setLastEdit(LocalDateTime.now());

        if (request.getTaskId() != null) {
            Task task = taskRepository.findById(request.getTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            comment.setTask(task);
        }

        if (request.getProjectId() != null) {
            Plan plan = planRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            comment.setPlan(plan);
        }

        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));
        comment.setAuthor(author);

        commentRepository.save(comment);

        return toResponse(comment);
    }

    @Override
    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return toResponse(comment);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CommentResponse updateComment(Long id, AddCommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(request.getContent());

        if (request.getTaskId() != null) {
            Task task = taskRepository.findById(request.getTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found"));
            comment.setTask(task);
        }

        if (request.getProjectId() != null) {
            Plan plan = planRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            comment.setPlan(plan);
        }

        if (request.getAuthorId() != null) {
            User author = userRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            comment.setAuthor(author);
        }

        commentRepository.save(comment);

        return toResponse(comment);
    }

    @Override
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found");
        }
        commentRepository.deleteById(id);
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getLastEdit(),
                comment.getAuthor() != null ? comment.getAuthor().getUsername() : null,
                comment.getTask() != null ? comment.getTask().getId() : null,
                comment.getPlan() != null ? comment.getPlan().getId() : null
        );
    }
}
