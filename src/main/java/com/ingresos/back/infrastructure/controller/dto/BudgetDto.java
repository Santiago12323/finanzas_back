package com.ingresos.back.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingresos.back.domain.enums.budget.BudgetStatus;
import com.ingresos.back.domain.valueobjects.Balance;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "BudgetDto", description = "DTO para la representación de un presupuesto de usuario")
public class BudgetDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único del presupuesto (generado automáticamente)", example = "e1f2g3h4-i5j6-7890-1234-567890abcdef", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Nombre del presupuesto", example = "Presupuesto de comida", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Notas adicionales del presupuesto", example = "Mercado y domicilios", nullable = true)
    private String note;

    @Schema(description = "ID de la categoría de gastos. Si es nulo, el presupuesto es global y cuenta todos los gastos del mes", example = "a7b8c9d0-1111-2222-3333-444455556666", nullable = true)
    private String categoryId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Nombre de la categoría (calculado)", example = "Comida", accessMode = Schema.AccessMode.READ_ONLY)
    private String categoryName;

    @Schema(description = "Límite mensual del presupuesto", requiredMode = Schema.RequiredMode.REQUIRED)
    private Balance monthlyLimit;

    @Schema(description = "Porcentaje de uso a partir del cual el estado pasa a WARNING (por defecto 80)", example = "80", nullable = true)
    private Integer alertThreshold;

    @Schema(description = "Si el presupuesto está activo (por defecto true)", example = "true", nullable = true)
    private Boolean active;

    @Schema(description = "ID de la cuenta asociada (opcional)", nullable = true)
    private String accountId;

    @Schema(description = "ID del usuario al que pertenece el presupuesto", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Gasto acumulado de la categoría en el mes consultado (calculado)", accessMode = Schema.AccessMode.READ_ONLY)
    private Balance totalSpent;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Monto restante del límite; negativo si se excedió (calculado)", example = "350000.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal remaining;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Porcentaje del límite consumido (calculado)", example = "65.50", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal percentUsed;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "OK, WARNING (superó el porcentaje de alerta) o EXCEEDED (superó el límite) (calculado)", example = "OK", accessMode = Schema.AccessMode.READ_ONLY)
    private BudgetStatus status;
}