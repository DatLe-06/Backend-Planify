package org.example.backend.service.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.example.backend.dto.auth.RegisterRequest;
import org.example.backend.dto.user.ProfileResponse;
import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.example.backend.exception.custom.EmailDuplicateException;
import org.example.backend.exception.custom.RoleNotFoundException;
import org.example.backend.exception.custom.UploadException;
import org.example.backend.exception.custom.UserNotFoundException;
import org.example.backend.mapper.UserMapper;
import org.example.backend.repository.RoleRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.UploadService;
import org.example.backend.utils.MessageUtils;
import org.example.backend.utils.Utils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService, UserService {
    private final UserMapper userMapper;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
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
    public ProfileResponse add(RegisterRequest request) {
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException(messageUtils.getMessage("role.not.found")));
        if (userRepository.existsByEmail((request.getEmail()))) {
            throw new EmailDuplicateException(messageUtils.getMessage("email.duplicate"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setBirthDate(request.getBirthDate());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getAvatar() != null) {
            String avatarPublicId = uploadService.upload(request.getAvatar(), user, "avatars");
            user.setAvatarPublicId(avatarPublicId);
        }
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(role);
        return userMapper.toResponse(userRepository.save(user), uploadService.buildCloudinaryUrl(user.getAvatarPublicId()));
    }

    @Override
    public ProfileResponse uploadAvatar(MultipartFile file) {
        User user = getCurrentUser();
        if (user.getAvatarPublicId() != null) uploadService.delete(user.getAvatarPublicId());
        String avatarPublicId = uploadService.upload(file, user, "avatars");
        user.setAvatarPublicId(avatarPublicId);
        return userMapper.toResponse(userRepository.save(user), uploadService.buildCloudinaryUrl(user.getAvatarPublicId()));
    }
}
