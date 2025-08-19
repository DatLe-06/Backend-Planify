package org.example.backend.service.user;

import org.example.backend.dto.auth.RegisterRequest;
import org.example.backend.dto.user.ProfileResponse;
import org.example.backend.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface UserService {
    User getCurrentUser();
    ProfileResponse getProfile(Long id);
    Set<User> getAllByIds(Set<Long> ids);
    ProfileResponse add(RegisterRequest request);
    ProfileResponse uploadAvatar(MultipartFile file);
}
