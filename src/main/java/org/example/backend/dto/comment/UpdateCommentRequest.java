package org.example.backend.dto.comment;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateCommentRequest {
    @Size(min = 1, message = "{comment.content.invalid}")
    private String content;

    @Size(min = 1, message = "{comment.mention.invalid}")
    private Set<@NotNull @Positive Long> mentionIds;
}
