package org.example.backend.mapper;

import org.example.backend.dto.comment.CommentResponse;
import org.example.backend.dto.comment.CreateCommentRequest;
import org.example.backend.dto.comment.UpdateCommentRequest;
import org.example.backend.entity.Comment;
import org.example.backend.entity.Plan;
import org.example.backend.entity.Task;
import org.example.backend.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class CommentMapper {
    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getLastEdit(),
                comment.getCreator().getUsername(),
                comment.getTask() != null ? comment.getTask().getId() : null,
                comment.getPlan() != null ? comment.getPlan().getId() : null
        );
    }

    public Comment toEntityForCreate(CreateCommentRequest request, Task task, Plan plan, Set<User> mentions, User creator) {
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setLastEdit(null);
        if (request.getTaskId() != null) comment.setTask(task);
        if (request.getPlanId() != null) comment.setPlan(plan);
        if (request.getMentionIds() != null) comment.setMentions(mentions);
        comment.setCreator(creator);
        return comment;
    }

    public void toEntityForUpdate(UpdateCommentRequest request, Set<User> mentions, Comment comment) {
        if (request.getContent() != null) comment.setContent(request.getContent());
        if (request.getMentionIds() != null) comment.setMentions(mentions);
        if (request.getContent() != null || request.getMentionIds() != null) comment.setLastEdit(LocalDateTime.now());
    }
}
