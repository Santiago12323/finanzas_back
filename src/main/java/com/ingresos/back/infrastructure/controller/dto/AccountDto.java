package com.ingresos.back.infrastructure.controller.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingresos.back.domain.enums.account.AccountType;
import com.ingresos.back.domain.enums.account.BankType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "AccountDto", description = "DTO para la representación de una cuenta de usuario")
public class AccountDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único de la cuenta (generado automáticamente)", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Balance actual de la cuenta", example = "1500.75", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal balance;

    @Schema(description = "Últimos 4 dígitos de la cuenta", example = "1234", nullable = true)
    private String last4;

    @Schema(description = "Tipo de banco asociado a la cuenta", example = "Banco Nacional", nullable = true)
    private BankType bankType;

    @Schema(description = "Tipo de cuenta (ej. Ahorro, Corriente)", example = "Ahorro", requiredMode = Schema.RequiredMode.REQUIRED)
    private AccountType accountType;

    @Schema(description = "ID del usuario al que pertenece la cuenta", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;
}
