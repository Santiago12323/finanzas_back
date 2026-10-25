package com.ingresos.back.infrastructure.controller.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "AuthResponse", description = "DTO para la respuesta de autenticación con tokens de acceso y refresco")
public class AuthResponse {
    @Schema(description = "ID del usuario", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private String userId;
    @Schema(description = "Token de acceso JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    @Schema(description = "Token de refresco JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
    @Schema(description = "Tipo de token", example = "Bearer")
    private String tokenType;
    @Schema(description = "Tiempo de expiración del token de acceso en segundos", example = "3600")
    private long expiresIn;
}
