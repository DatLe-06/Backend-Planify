package org.example.backend.dto.comment;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateCommentRequest {
    @NotBlank(message = "{comment.content.invalid}")
    private String content;

    private Long taskId;
    private Long planId;

    @Size(min = 1, message = "{comment.mention.invalid}")
    private Set<@NotNull @Positive Long> mentionIds;

    @AssertTrue(message = "{comment.target.invalid}")
    public boolean isValid() {
        return (taskId != null) ^ (planId != null);
    }
}
