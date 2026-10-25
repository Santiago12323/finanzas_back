package com.ingresos.back.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "UserDto", description = "DTO que representa la información de un usuario")
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único del usuario (generado automáticamente)", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Nombre completo del usuario", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Correo electrónico del usuario", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;
}
