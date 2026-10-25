package com.ingresos.back.infrastructure.controller.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "RegisterDto", description = "DTO para el registro de un nuevo usuario")
public class RegisterDto {
    @Schema(description = "Nombre del usuario", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Correo electrónico del usuario", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Contraseña del usuario", example = "securePassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
