package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.port.AuthService;
import com.ingresos.back.domain.port.JwtService;
import com.ingresos.back.domain.port.PasswordHasher;
import com.ingresos.back.infrastructure.controller.dto.auth.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordHasher passwordHasher;

    @Autowired
    private JwtService jwtService;

    @Override
    public AuthResponse login(
            String email,
            String password
    ) {

        User user = userService.findByEmail(email);

        if (!passwordHasher.matches(
                password,
                user.getPassword()
        )) {
            throw new RuntimeException(
                    "Email o contraseña incorrectos"
            );
        }

        String accessToken =
                jwtService.generateAccessToken(user);

        String refreshToken =
                jwtService.generateRefreshToken(user);

        return new AuthResponse(
                user.getId(),
                accessToken,
                refreshToken,
                "Bearer",
                900
        );
    }

    @Override
    public AuthResponse refreshToken(
            String refreshToken
    ) {

        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new RuntimeException(
                    "Refresh token inválido o expirado"
            );
        }

        String email =
                jwtService.getEmailFromRefreshToken(
                        refreshToken
                );

        User user = userService.findByEmail(email);

        String newAccessToken =
                jwtService.generateAccessToken(user);

        return new AuthResponse(
                user.getId(),
                newAccessToken,
                refreshToken,
                "Bearer",
                900
        );
    }

    @Override
    public User register(
            String name,
            String email,
            String password
    ) {

        if (userService.existsByEmail(email)) {
            throw new RuntimeException(
                    "El email ya está registrado"
            );
        }

        String passwordHash =
                passwordHasher.hash(password);

        User user = new User(
                name,
                email,
                passwordHash
        );

        return userService.createUser(user);
    }

    @Override
    public void forgotPassword(
            String email
    ) {

        userService.findByEmail(email);

        // Próximamente:
        // generar token de recuperación
        // guardar token
        // enviar correo
    }
}
