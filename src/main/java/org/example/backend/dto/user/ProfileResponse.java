package org.example.backend.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileResponse {
    private long id;
    private String username;
    private LocalDate birthDate;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
