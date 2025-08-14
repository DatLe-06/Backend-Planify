package org.example.backend.dto.status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddStatusRequest {
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 20, message = "Name cannot exceed 20 characters")
    private String name;

    private String description;

    @NotNull(message = "User ID cannot be null")
    private Long userId;
}
