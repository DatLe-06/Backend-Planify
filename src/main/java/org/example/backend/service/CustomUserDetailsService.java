package org.example.backend.service;

import lombok.AllArgsConstructor;
import org.aspectj.bridge.MessageUtil;
import org.example.backend.dto.auth.RegisterRequest;
import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.example.backend.exception.custom.EmailDuplicateException;
import org.example.backend.exception.custom.RoleNotFoundException;
import org.example.backend.exception.custom.UserNotFoundException;
import org.example.backend.repository.RoleRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.utils.MessageUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private MessageUtils messageUtils;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(messageUtils.getMessage("user.not.found")));
    }

    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(messageUtils.getMessage("user.not.found")));
    }

    public void add(RegisterRequest request) {
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
        user.setAvatarUrl(request.getAvatarUrl());
        user.setEnabled(request.isEnabled());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(role);
        userRepository.save(user);
    }
}
