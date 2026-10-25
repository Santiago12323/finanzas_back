package com.ingresos.back.domain.enums.account;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de cuenta bancaria o financiera")
public enum AccountType {
    @Schema(description = "Cuenta de ahorros")
    CUENTA_DE_AHORROS,
    @Schema(description = "Cuenta corriente")
    CUENTA_CORRIENTE,
    @Schema(description = "Billetera digital")
    BILLETERA_DIGITAL,
    @Schema(description = "Tarjeta de crédito")
    TARJETA_DE_CREDITO,
    @Schema(description = "Efectivo")
    EFECTIVO,
    @Schema(description = "Inversión")
    INVERSION
}