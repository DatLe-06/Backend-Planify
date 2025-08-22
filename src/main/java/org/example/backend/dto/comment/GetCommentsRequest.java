package org.example.backend.dto.comment;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCommentsRequest {
    private Long taskId;
    private Long planId;

    @AssertTrue(message = "{comment.target.invalid}")
    public boolean isValid() {
        return (taskId == null ^ planId == null);
    }
}
