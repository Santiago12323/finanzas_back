package com.ingresos.back.infrastructure.controller.dto.suscription;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingresos.back.domain.enums.account.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String name;
    private String description;
    private BigDecimal amount;
    private Currency currency;
    private LocalDate nextPaymentDate;
    private Boolean automated;
    private String userId;
}