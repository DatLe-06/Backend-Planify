package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.dto.auth.AuthRequest;
import org.example.backend.dto.auth.AuthResponse;
import org.example.backend.dto.auth.LogoutRequest;
import org.example.backend.dto.auth.RegisterRequest;
import org.example.backend.entity.User;
import org.example.backend.service.user.CustomUserDetailsService;
import org.example.backend.service.JwtService;
import org.example.backend.service.RefreshTokenService;
import org.example.backend.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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
            final User user = (User) userDetailsService.loadUserByUsername(request.getEmail());
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageUtils.getMessage("internal.server.error"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequest request) {
        refreshTokenService.remove(request.getEmail());
        return ResponseEntity.ok(messageUtils.getMessage("logout.success"));
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@Valid @ModelAttribute RegisterRequest request) {;
        return ResponseEntity.ok(userDetailsService.add(request));
    }}

