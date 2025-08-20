package org.example.backend.service.auth;

import lombok.AllArgsConstructor;
import org.example.backend.constant.CloudinaryTarget;
import org.example.backend.constant.RoleName;
import org.example.backend.dto.auth.AuthRequest;
import org.example.backend.dto.auth.AuthResponse;
import org.example.backend.dto.auth.RegisterRequest;
import org.example.backend.dto.user.ProfileResponse;
import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.example.backend.exception.custom.EmailDuplicateException;
import org.example.backend.exception.custom.RoleNotFoundException;
import org.example.backend.mapper.UserMapper;
import org.example.backend.repository.RoleRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.JwtService;
import org.example.backend.service.RefreshTokenService;
import org.example.backend.service.UploadService;
import org.example.backend.service.user.CustomUserDetailsService;
import org.example.backend.utils.MessageUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {
    private AuthenticationManager authManager;
    private JwtService jwtService;
    private CustomUserDetailsService userDetailsService;
    private RefreshTokenService refreshTokenService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UploadService uploadService;
    private UserMapper userMapper;
    private MessageUtils messageUtils;

    public AuthResponse login(AuthRequest request) {
        final User user = (User) userDetailsService.loadUserByUsername(request.getEmail());
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        final String token = jwtService.generateToken(user);
//        refreshTokenService.add(user.getId());
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        return new AuthResponse(token);
    }

    public ProfileResponse add(RegisterRequest request) {
        Role role = roleRepository.findByName(RoleName.ROLE_USER)
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
            String avatarPublicId = uploadService.upload(request.getAvatar(), user, CloudinaryTarget.AVATAR);
            user.setAvatarPublicId(avatarPublicId);
        }
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(role);
        return userMapper.toResponse(userRepository.save(user), uploadService.buildCloudinaryUrl(user.getAvatarPublicId()));
    }
}
