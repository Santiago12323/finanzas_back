package com.ingresos.back.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingresos.back.domain.enums.account.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(name = "IncomeDto", description = "DTO para la representación de un ingreso de usuario")
public class IncomeDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único del ingreso (generado automáticamente)", example = "c1d2e3f4-a5b6-7890-1234-567890abcdef", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Descripción del ingreso", example = "Salario mensual", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "Monto del ingreso", example = "2500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @Schema(description = "Moneda del ingreso", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"USD", "EUR", "GBP", "JPY", "ARS"})
    private Currency currency;

    @Schema(description = "ID de la categoría del ingreso. Debe ser de tipo INCOME o BOTH, por defecto del sistema o creada por el mismo usuario (ver GET /api/categories/user/{userId}/type/INCOME)", example = "a7b8c9d0-1111-2222-3333-444455556666", requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoryId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(description = "Fecha del ingreso (formato DD-MM-YYYY)", example = "23-10-2026", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate incomeDate;

    @Schema(description = "ID de la cuenta que recibe el ingreso", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountId;

    @Schema(description = "ID del usuario que registra el ingreso", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;
}