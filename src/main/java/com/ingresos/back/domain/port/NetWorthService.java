package com.ingresos.back.domain.port;

import com.ingresos.back.infrastructure.controller.dto.NetWorthDto;

import java.time.LocalDate;

public interface NetWorthService {

    NetWorthDto calculateNetWorth(String userId, LocalDate from, LocalDate to);
}
