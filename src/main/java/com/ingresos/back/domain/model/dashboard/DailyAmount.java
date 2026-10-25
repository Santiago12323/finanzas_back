package com.ingresos.back.domain.model.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyAmount(LocalDate date, BigDecimal amount) {}