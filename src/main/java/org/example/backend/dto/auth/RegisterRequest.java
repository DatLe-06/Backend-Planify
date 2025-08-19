package org.example.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest extends BaseUserRequest{
    @NotBlank(message = "Username cannot be empty")
    private String username;
    private LocalDate birthDate;
    private MultipartFile avatar;
}

