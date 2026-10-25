package com.ingresos.back.infrastructure.controller.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "LoginDto", description = "DTO para las credenciales de inicio de sesión")
public class LoginDto {
    @Schema(description = "Correo electrónico del usuario", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Contraseña del usuario", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
