package org.example.backend.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCommentRequest {
    private String content;
    private Long taskId;
    private Long projectId;
    private Long authorId;
}
