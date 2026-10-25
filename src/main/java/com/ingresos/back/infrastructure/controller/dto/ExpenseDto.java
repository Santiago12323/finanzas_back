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
@Schema(name = "ExpenseDto", description = "DTO para la representación de un gasto de usuario")
public class ExpenseDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único del gasto (generado automáticamente)", example = "b1c2d3e4-f5a6-7890-1234-567890abcdef", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Descripción del gasto", example = "Compra de supermercado", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "Monto del gasto", example = "75.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @Schema(description = "Moneda del gasto", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"USD", "EUR", "GBP", "JPY", "ARS"})
    private Currency currency;

    @Schema(description = "ID de la categoría del gasto. Debe ser una categoría de tipo EXPENSE o BOTH, por defecto del sistema o creada por el mismo usuario (ver GET /api/categories/user/{userId}?type=EXPENSE)", example = "a7b8c9d0-1111-2222-3333-444455556666", requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoryId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(description = "Fecha del gasto (formato DD-MM-YYYY)", example = "23-10-2026", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate expenseDate;

    @Schema(description = "ID de la cuenta asociada al gasto", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountId;

    @Schema(description = "ID del usuario que realizó el gasto", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;
}