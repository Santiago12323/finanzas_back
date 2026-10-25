package com.ingresos.back.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingresos.back.domain.enums.category.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "CategoryDto", description = "DTO para la representación de una categoría")
public class CategoryDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único de la categoría (generado automáticamente)", example = "a7b8c9d0-1111-2222-3333-444455556666", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Nombre de la categoría", example = "Comida", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Tipo: EXPENSE (gastos), INCOME (ingresos) o BOTH (ambos)", example = "EXPENSE", requiredMode = Schema.RequiredMode.REQUIRED)
    private CategoryType type;

    @Schema(description = "Nombre del ícono para el front", example = "utensils", nullable = true)
    private String icon;

    @Schema(description = "Color en hexadecimal", example = "#F97316", nullable = true)
    private String color;

    @Schema(description = "ID del usuario dueño. Nulo en las categorías por defecto del sistema", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private String userId;
}