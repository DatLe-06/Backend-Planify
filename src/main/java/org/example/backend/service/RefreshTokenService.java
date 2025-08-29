package org.example.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend.entity.RefreshToken;
import org.example.backend.entity.User;
import org.example.backend.exception.custom.UserNotFoundException;
import org.example.backend.repository.RefreshTokenRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.utils.OtherUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${characters}")
    private String characters;

    @Value("${refreshtoken.length}")
    private long length;

    @Value("${refreshtoken.expiry.date}")
    private long timeActive;

    public boolean isValidRefreshToken(String email) {
        return refreshTokenRepository.existsByUserEmailAndExpiryDateAfter(email, LocalDateTime.now());
    }

    @Transactional
    public void add(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (refreshTokenRepository.existsByUserEmail(user.getEmail())) {
            refreshTokenRepository.removeRefreshTokenByUserEmail(user.getEmail());
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(timeActive));

        String token;
        do {
            token = OtherUtils.generateRefreshToken(length, characters);
        } while (refreshTokenRepository.existsRefreshTokenByToken(token));
        refreshToken.setToken(token);

        refreshTokenRepository.save(refreshToken);
    }

    public void remove(String email) {
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUserEmail(email);
        if (refreshToken == null) throw new RuntimeException("Logout fail");
        refreshTokenRepository.delete(refreshToken);
    }
}
