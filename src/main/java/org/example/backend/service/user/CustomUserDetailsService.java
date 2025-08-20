package org.example.backend.service.user;

import lombok.AllArgsConstructor;
import org.example.backend.constant.CloudinaryTarget;
import org.example.backend.dto.user.ProfileResponse;
import org.example.backend.entity.User;
import org.example.backend.exception.custom.UserNotFoundException;
import org.example.backend.mapper.UserMapper;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.UploadService;
import org.example.backend.utils.MessageUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService, UserService {
    private final UserMapper userMapper;
    private UserRepository userRepository;
    private UploadService uploadService;
    private MessageUtils messageUtils;

    @Override
    public User getCurrentUser() {
        return  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public ProfileResponse getProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(messageUtils.getMessage("user.not.found")));
        String avatarUrl = uploadService.buildCloudinaryUrl(user.getAvatarPublicId());
        return userMapper.toResponse(user, avatarUrl);
    }

    @Override
    public Set<User> getAllByIds(Set<Long> ids) {
        return new HashSet<>(userRepository.findAllById(ids));
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(messageUtils.getMessage("user.not.found")));
    }

    @Override
    public ProfileResponse uploadAvatar(MultipartFile file) {
        User user = getCurrentUser();
        if (user.getAvatarPublicId() != null) uploadService.delete(user.getAvatarPublicId());
        String avatarPublicId = uploadService.upload(file, user, CloudinaryTarget.AVATAR);
        user.setAvatarPublicId(avatarPublicId);
        return userMapper.toResponse(userRepository.save(user), uploadService.buildCloudinaryUrl(user.getAvatarPublicId()));
    }
}
