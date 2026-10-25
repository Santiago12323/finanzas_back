package com.ingresos.back.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingresos.back.domain.valueobjects.Balance;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "BudgetExpenseDto", description = "DTO para la representación de un gasto dentro de un presupuesto")
public class BudgetExpenseDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único del gasto (generado automáticamente)", example = "j1k2l3m4-n5o6-7890-1234-567890abcdef", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Descripción del gasto", example = "Compra de supermercado", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "Valor del gasto", requiredMode = Schema.RequiredMode.REQUIRED)
    private Balance value;
}
