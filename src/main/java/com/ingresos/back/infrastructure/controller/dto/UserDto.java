package com.ingresos.back.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String name;
    private String email;
}
