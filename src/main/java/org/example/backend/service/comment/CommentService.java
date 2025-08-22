package org.example.backend.service.comment;

import org.example.backend.dto.comment.CommentResponse;
import org.example.backend.dto.comment.CreateCommentRequest;
import org.example.backend.dto.comment.GetCommentsRequest;
import org.example.backend.dto.comment.UpdateCommentRequest;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface CommentService {
    CommentResponse createComment(CreateCommentRequest request);
    CommentResponse getCommentById(Long id);
    CommentResponse updateComment(Long id, UpdateCommentRequest request);
    void deleteComment(Long id);
    Set<CommentResponse> getAllComments(GetCommentsRequest request, Pageable pageable);
}
