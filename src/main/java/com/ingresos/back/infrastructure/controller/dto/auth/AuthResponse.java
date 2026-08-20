package com.ingresos.back.infrastructure.controller.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String userId;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;
}
