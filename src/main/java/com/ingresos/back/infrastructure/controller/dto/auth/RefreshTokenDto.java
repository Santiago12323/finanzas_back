package com.ingresos.back.infrastructure.controller.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "RefreshTokenDto", description = "DTO para solicitar un nuevo token de acceso usando un token de refresco")
public class RefreshTokenDto {
    @Schema(description = "Token de refresco JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;
}
