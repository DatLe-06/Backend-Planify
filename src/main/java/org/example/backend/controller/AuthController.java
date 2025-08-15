package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.dto.auth.AuthRequest;
import org.example.backend.dto.auth.AuthResponse;
import org.example.backend.dto.auth.LogoutRequest;
import org.example.backend.dto.auth.RegisterRequest;
import org.example.backend.entity.User;
import org.example.backend.service.CustomUserDetailsService;
import org.example.backend.service.JwtService;
import org.example.backend.service.RefreshTokenService;
import org.example.backend.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthenticationManager authManager;
    private JwtService jwtService;
    private CustomUserDetailsService userDetailsService;
    private RefreshTokenService refreshTokenService;
    private MessageUtils messageUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            final User user = userDetailsService.loadUserByEmail(request.getEmail());
            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            final String token = jwtService.generateToken(user);
            refreshTokenService.add(user.getId());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageUtils.getMessage("user.not.found"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageUtils.getMessage("invalid.credentials"));
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageUtils.getMessage("user.disabled"));
        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageUtils.getMessage("internal.server.error"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequest request) {
        refreshTokenService.remove(request.getEmail());
        return ResponseEntity.ok(messageUtils.getMessage("logout.success"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        userDetailsService.add(request);
        return ResponseEntity.ok(messageUtils.getMessage("register.success"));
    }}

