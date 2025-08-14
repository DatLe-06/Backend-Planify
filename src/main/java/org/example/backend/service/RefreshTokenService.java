package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.entity.RefreshToken;
import org.example.backend.entity.User;
import org.example.backend.exception.custom.UserNotFoundException;
import org.example.backend.repository.RefreshTokenRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
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

    public boolean existValidRefreshToken(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        return refreshTokenRepository.findRefreshTokenByExpiryDateAfterAndUser(LocalDateTime.now(),user);
    }

    public void add(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(timeActive));

        String token;
        do {
            token = Utils.generateRefreshToken(length, characters);
        } while (refreshTokenRepository.existsRefreshTokenByToken(token));
        refreshToken.setToken(token);

        refreshTokenRepository.save(refreshToken);
    }

    public void remove(String username) {
        refreshTokenRepository.removeRefreshTokenByUserUsername(username);
    }
}
