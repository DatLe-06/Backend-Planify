package org.example.backend.service.comment;

import org.example.backend.dto.comment.AddCommentRequest;
import org.example.backend.dto.comment.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(AddCommentRequest request);
    CommentResponse getCommentById(Long id);
    List<CommentResponse> getAllComments();
    CommentResponse updateComment(Long id, AddCommentRequest request);
    void deleteComment(Long id);
}
