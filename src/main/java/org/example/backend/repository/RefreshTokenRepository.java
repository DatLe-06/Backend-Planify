package org.example.backend.repository;

import org.example.backend.entity.RefreshToken;
import org.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    boolean findRefreshTokenByToken(String token);

    boolean existsRefreshTokenByToken(String token);

    List<RefreshToken> findRefreshTokenByExpiryDateBeforeAndUser(LocalDateTime expiryDateBefore, User user);

    boolean findRefreshTokenByExpiryDateAfterAndUser(LocalDateTime expiryDateAfter, User user);

    void removeRefreshTokenByUser_Id(long userId);

    void removeRefreshTokenByUserUsername(String userUsername);
}
