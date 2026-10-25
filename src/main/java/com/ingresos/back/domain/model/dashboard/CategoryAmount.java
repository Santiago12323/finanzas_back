package com.ingresos.back.domain.model.dashboard;

import java.math.BigDecimal;

public record CategoryAmount(String categoryId, String name, String icon, String color, BigDecimal amount) {}