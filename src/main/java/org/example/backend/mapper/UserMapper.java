package org.example.backend.mapper;

import org.example.backend.dto.user.ProfileResponse;
import org.example.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public ProfileResponse toResponse(User user, String avatarUrl) {
        ProfileResponse response = new ProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        if (avatarUrl != null) response.setAvatarUrl(avatarUrl);
        response.setCreatedAt(user.getCreatedAt());
        response.setLastLoginAt(user.getLastLoginAt());
        return response;
    }
}
