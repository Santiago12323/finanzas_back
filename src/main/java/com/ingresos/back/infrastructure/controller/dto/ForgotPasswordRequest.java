package com.ingresos.back.infrastructure.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "ForgotPasswordRequest", description = "DTO para solicitar un restablecimiento de contraseña")
public class ForgotPasswordRequest {
    @Schema(description = "Correo electrónico del usuario para restablecer la contraseña", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}
