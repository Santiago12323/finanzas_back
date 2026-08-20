package com.ingresos.back.domain.port;

import com.ingresos.back.domain.model.User;

public interface JwtService {
    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String getEmailFromAccessToken(String token);

    String getEmailFromRefreshToken(String token);

    boolean isAccessTokenValid(String token);

    boolean isRefreshTokenValid(String token);
}
