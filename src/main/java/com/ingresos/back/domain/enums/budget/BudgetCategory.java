package com.ingresos.back.domain.enums.budget;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categorías de presupuesto")
public enum BudgetCategory {
    @Schema(description = "Gastos relacionados con vivienda")
    VIVIENDA,
    @Schema(description = "Gastos de alimentación")
    ALIMENTACION,
    @Schema(description = "Gastos de transporte")
    TRANSPORTE,
    @Schema(description = "Gastos de entretenimiento")
    ENTRETENIMIENTO,
    @Schema(description = "Gastos de salud")
    SALUD,
    @Schema(description = "Gastos de educación")
    EDUCACION,
    @Schema(description = "Gastos de servicios básicos")
    SERVICIOS,
    @Schema(description = "Gastos de mascotas")
    MASCOTAS,
    @Schema(description = "Gastos de compras en general")
    COMPRAS,
    @Schema(description = "Presupuesto destinado al ahorro")
    AHORRO,
    @Schema(description = "Presupuesto destinado a inversión")
    INVERSION,
    @Schema(description = "Otras categorías de presupuesto")
    OTROS

}