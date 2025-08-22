package org.example.backend.service.comment;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.backend.dto.comment.CreateCommentRequest;
import org.example.backend.dto.comment.CommentResponse;

import java.util.*;

import org.example.backend.dto.comment.GetCommentsRequest;
import org.example.backend.dto.comment.UpdateCommentRequest;
import org.example.backend.entity.Comment;
import org.example.backend.entity.Plan;
import org.example.backend.entity.Task;
import org.example.backend.entity.User;
import org.example.backend.mapper.CommentMapper;
import org.example.backend.repository.CommentRepository;
import org.example.backend.repository.PlanRepository;
import org.example.backend.repository.TaskRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.user.CustomUserDetailsService;
import org.example.backend.utils.MessageUtils;
import org.example.backend.validation.PermissionValidate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final PlanRepository planRepository;
    private final CommentMapper commentMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final PermissionValidate permissionValidate;
    private final MessageUtils messageUtils;

    @Override
    public CommentResponse createComment(CreateCommentRequest request) {
        User creator = customUserDetailsService.getCurrentUser();
        Task task = taskRepository.findByIdWithPlan(request.getTaskId()).orElse(null);
        Plan plan = planRepository.findByIdWithOwner(request.getPlanId()).orElse(null);
        Set<User> mentions = request.getMentionIds() != null
                ? new HashSet<>(userRepository.findAllById(request.getMentionIds()))
                : new HashSet<>();

        Comment comment = commentMapper.toEntityForCreate(request, task, plan, mentions, creator);
        permissionValidate.canCreateComment(plan, task, creator);
        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public CommentResponse updateComment(Long id, UpdateCommentRequest request) {
        User creator = customUserDetailsService.getCurrentUser();
        Comment comment = commentRepository.findByIdWithData(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("comment.not.found")));

        Set<User> mentions = request.getMentionIds() != null
                ? new HashSet<>(userRepository.findAllById(request.getMentionIds()))
                : new HashSet<>();
        Task task = taskRepository.findByIdWithPlan(comment.getTask().getId())
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("task.not.found")));

        commentMapper.toEntityForUpdate(request, mentions, comment);
        permissionValidate.canUpdateAndDeleteComment(comment, creator, comment.getPlan(), task);
        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long id) {
        User creator = customUserDetailsService.getCurrentUser();
        Comment comment = commentRepository.findByIdWithData(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("comment.not.found")));
        Task task = taskRepository.findByIdWithPlan(comment.getTask().getId())
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("task.not.found")));
        permissionValidate.canUpdateAndDeleteComment(comment, creator, comment.getPlan(), task);
        commentRepository.deleteById(id);
    }

    @Override
    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(messageUtils.getMessage("comment.not.found")));
        return commentMapper.toResponse(comment);
    }

    @Override
    public Set<CommentResponse> getAllComments(GetCommentsRequest request, Pageable pageable) {
        User user = customUserDetailsService.getCurrentUser();
        Task task = taskRepository.findByIdWithPlan(request.getTaskId()).orElse(null);
        Plan plan = planRepository.findByIdWithOwner(request.getPlanId()).orElse(null);

        permissionValidate.canViewComment(plan, task, user);
        List<Comment> comments;
        if (request.getPlanId() != null) {
            comments = commentRepository.findAllByPlanOrderByCreatedAtDesc(plan, pageable);
        } else {
            comments = commentRepository.findAllByTaskOrderByCreatedAtDesc(task, pageable);
        }

        return comments.stream().map(commentMapper::toResponse).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
