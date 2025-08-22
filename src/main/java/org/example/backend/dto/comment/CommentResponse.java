package org.example.backend.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime lastEdit;
    private String creatorName;
    private Long taskId;
    private Long planId;
}
