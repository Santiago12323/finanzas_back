package com.ingresos.back.infrastructure.controller.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private BigDecimal balance;
    private String last4;
    private String bankType;
    private String accountType;
    private String userId;
}