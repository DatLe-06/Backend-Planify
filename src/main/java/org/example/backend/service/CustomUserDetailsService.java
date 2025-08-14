package org.example.backend.service;

import lombok.AllArgsConstructor;
import org.example.backend.dto.auth.RegisterRequest;
import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.example.backend.repository.RoleRepository;
import org.example.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void add(RegisterRequest request) {
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("role not found"));
        if (userRepository.existsUserByUsername(request.getUsername())) {
            throw new RuntimeException("username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRole(role);
        userRepository.save(user);
    }
}
