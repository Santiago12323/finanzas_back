package com.ingresos.back.domain.port;

import com.ingresos.back.domain.model.User;
import com.ingresos.back.infrastructure.controller.dto.auth.AuthResponse;

public interface AuthService {
    AuthResponse login(String email, String password);

    AuthResponse loginWithGoogle(
            String idToken
    );

    AuthResponse refreshToken(
            String refreshToken
    );

    User register(
            String name,
            String email,
            String password
    );

    void forgotPassword(String email);
}
