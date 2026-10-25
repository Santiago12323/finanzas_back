package com.ingresos.back.infrastructure.controller.dto.suscription;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.enums.subscription.SubscriptionFrequency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "SubscriptionDto", description = "DTO para la representación de una suscripción de usuario")
public class SubscriptionDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único de la suscripción (generado automáticamente)", example = "d1e2f3g4-h5i6-7890-1234-567890abcdef", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Nombre de la suscripción", example = "Netflix", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Descripción de la suscripción", example = "Servicio de streaming de películas y series", nullable = true)
    private String description;

    @Schema(description = "Monto de la suscripción", example = "15.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @Schema(description = "Moneda de la suscripción", example = "USD", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"USD", "EUR", "GBP", "JPY", "ARS"})
    private Currency currency;

    @Schema(description = "Fecha de inicio de la suscripción (DD-MM-YYYY)", example = "23-10-2026", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @Schema(description = "Fecha de fin de la suscripción (DD-MM-YYYY)", example = "23-10-2027", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @Schema(description = "Frecuencia de la suscripción", example = "MONTHLY", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"DAILY", "WEEKLY", "MONTHLY", "ANNUAL"})
    private SubscriptionFrequency frequency;

    @Schema(description = "Indica si la suscripción se automatiza", example = "true", nullable = true)
    private Boolean automated;

    @Schema(description = "ID del usuario que posee la suscripción", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;
}