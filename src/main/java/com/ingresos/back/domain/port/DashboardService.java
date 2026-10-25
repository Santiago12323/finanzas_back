package com.ingresos.back.domain.port;

import com.ingresos.back.domain.enums.dashboard.MovementType;
import com.ingresos.back.domain.model.dashboard.Dashboard;
import com.ingresos.back.domain.model.dashboard.DashboardQuery;

public interface DashboardService {
    Dashboard getDashboard(DashboardQuery query);

    Dashboard.Distribution getDistribution(DashboardQuery query, MovementType type);
}