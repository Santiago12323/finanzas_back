package com.ingresos.back.infrastructure.controller.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "GoogleLoginDto", description = "Datos para iniciar sesión con Google")
public class GoogleLoginDto {

    @Schema(description = "ID token (JWT) que entrega Google Sign-In en el front", example = "eyJhbGciOiJSUzI1NiIsImtpZCI6...", requiredMode = Schema.RequiredMode.REQUIRED)
    private String idToken;
}