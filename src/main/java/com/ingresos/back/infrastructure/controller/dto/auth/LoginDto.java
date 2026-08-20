package com.ingresos.back.infrastructure.controller.dto.auth;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
