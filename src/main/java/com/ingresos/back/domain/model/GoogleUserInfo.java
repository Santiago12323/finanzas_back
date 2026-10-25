package com.ingresos.back.domain.model;

import lombok.Value;

@Value
public class GoogleUserInfo {
    String email;
    boolean emailVerified;
    String name;
    String subject;
}