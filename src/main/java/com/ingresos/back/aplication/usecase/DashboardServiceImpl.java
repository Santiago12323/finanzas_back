package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.enums.dashboard.Granularity;
import com.ingresos.back.domain.enums.dashboard.MovementType;
import com.ingresos.back.domain.enums.dashboard.PeriodType;
import com.ingresos.back.domain.exception.DashboardException;
import com.ingresos.back.domain.model.dashboard.CategoryAmount;
import com.ingresos.back.domain.model.dashboard.DailyAmount;
import com.ingresos.back.domain.model.dashboard.Dashboard;
import com.ingresos.back.domain.model.dashboard.DashboardQuery;
import com.ingresos.back.domain.port.DashboardService;
import com.ingresos.back.domain.port.UseService;
import com.ingresos.back.domain.repository.ExpenseRepository;
import com.ingresos.back.domain.repository.IncomeRepository;
import com.ingresos.back.domain.valueobjects.PeriodRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final int MAX_BUCKETS = 400;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UseService useService;

    private record Totals(BigDecimal income, BigDecimal expense) {
        BigDecimal net() {
            return income.subtract(expense);
        }
    }

    private record Window(LocalDate from, LocalDate to) {
    }

    // ---------- API del port ----------

    @Override
    public Dashboard getDashboard(DashboardQuery query) {
        PeriodRange range = validateAndResolve(query);
        PeriodRange previousRange = range.previous();
        Granularity granularity = query.getGranularity() != null ? query.getGranularity() : range.defaultGranularity();

        Totals current = totals(query, range);
        Totals previous = totals(query, previousRange);

        return Dashboard.builder()
                .userId(query.getUserId())
                .period(range.getType())
                .currency(query.getCurrency())
                .range(toDateRange(range))
                .previousRange(toDateRange(previousRange))
                .summary(buildSummary(range, current, previous))
                .cashflow(buildCashflow(query, range, granularity))
                .expenseDistribution(buildDistribution(query, range, previousRange, MovementType.EXPENSE, current, previous))
                .incomeDistribution(buildDistribution(query, range, previousRange, MovementType.INCOME, current, previous))
                .build();
    }

    @Override
    public Dashboard.Distribution getDistribution(DashboardQuery query, MovementType type) {
        if (type == null) {
            throw new DashboardException("El tipo (EXPENSE o INCOME) es obligatorio");
        }
        PeriodRange range = validateAndResolve(query);
        PeriodRange previousRange = range.previous();
        return buildDistribution(query, range, previousRange, type, totals(query, range), totals(query, previousRange));
    }

    // ---------- Validación y totales ----------

    private PeriodRange validateAndResolve(DashboardQuery query) {
        useService.findById(query.getUserId());
        if (query.getCurrency() == null) {
            throw new DashboardException("La moneda es obligatoria");
        }
        if (query.getTopN() != null && query.getTopN() < 1) {
            throw new DashboardException("topN debe ser mayor o igual a 1");
        }
        PeriodType type = query.getPeriod() != null ? query.getPeriod() : PeriodType.MONTH;
        return PeriodRange.of(type, query.getDate(), query.getFrom(), query.getTo());
    }

    private Totals totals(DashboardQuery query, PeriodRange range) {
        BigDecimal income = incomeRepository.sumByUserBetween(
                query.getUserId(), query.getCurrency(), range.startDateTime(), range.endDateTime());
        BigDecimal expense = expenseRepository.sumByUserBetween(
                query.getUserId(), query.getCurrency(), range.startDateTime(), range.endDateTime());
        return new Totals(nz(income), nz(expense));
    }

    private Dashboard.DateRange toDateRange(PeriodRange range) {
        return Dashboard.DateRange.builder().from(range.getFrom()).to(range.getTo()).build();
    }

    // ---------- Resumen ----------

    private Dashboard.Summary buildSummary(PeriodRange range, Totals current, Totals previous) {
        LocalDate today = LocalDate.now();

        long elapsedDays = today.isBefore(range.getFrom())
                ? 0
                : ChronoUnit.DAYS.between(range.getFrom(), today.isAfter(range.getTo()) ? range.getTo() : today) + 1;
        BigDecimal avgDailyExpense = elapsedDays == 0
                ? BigDecimal.ZERO
                : current.expense().divide(BigDecimal.valueOf(elapsedDays), 2, RoundingMode.HALF_UP);
        BigDecimal projectedExpense = range.contains(today)
                ? avgDailyExpense.multiply(BigDecimal.valueOf(range.days())).setScale(2, RoundingMode.HALF_UP)
                : null;

        BigDecimal savingsRate = percent(current.net(), current.income());
        BigDecimal previousRate = percent(previous.net(), previous.income());

        Dashboard.Comparison comparison = Dashboard.Comparison.builder()
                .previousIncome(previous.income())
                .previousExpense(previous.expense())
                .previousNet(previous.net())
                .incomeVariation(variation(current.income(), previous.income()))
                .expenseVariation(variation(current.expense(), previous.expense()))
                .netVariation(variation(current.net(), previous.net()))
                .savingsRatePoints(savingsRate != null && previousRate != null ? savingsRate.subtract(previousRate) : null)
                .build();

        return Dashboard.Summary.builder()
                .totalIncome(current.income())
                .totalExpense(current.expense())
                .net(current.net())
                .savingsRate(savingsRate)
                .avgDailyExpense(avgDailyExpense)
                .projectedExpense(projectedExpense)
                .comparison(comparison)
                .build();
    }

    // ---------- Flujo ----------

    private Dashboard.Cashflow buildCashflow(DashboardQuery query, PeriodRange range, Granularity granularity) {
        List<Window> windows = windows(range, granularity);
        if (windows.size() > MAX_BUCKETS) {
            throw new DashboardException("El rango genera demasiados puntos con granularidad " + granularity
                    + ". Usa WEEK, MONTH o QUARTER");
        }

        Map<LocalDate, BigDecimal> incomeByDay = toMap(incomeRepository.sumGroupedByDay(
                query.getUserId(), query.getCurrency(), range.startDateTime(), range.endDateTime()));
        Map<LocalDate, BigDecimal> expenseByDay = toMap(expenseRepository.sumGroupedByDay(
                query.getUserId(), query.getCurrency(), range.startDateTime(), range.endDateTime()));

        List<Dashboard.CashflowBucket> buckets = new ArrayList<>();
        BigDecimal cumulative = BigDecimal.ZERO;
        for (Window window : windows) {
            BigDecimal income = sum(incomeByDay, window.from(), window.to());
            BigDecimal expense = sum(expenseByDay, window.from(), window.to());
            BigDecimal net = income.subtract(expense);
            cumulative = cumulative.add(net);
            buckets.add(Dashboard.CashflowBucket.builder()
                    .key(key(granularity, window.from()))
                    .from(window.from())
                    .to(window.to())
                    .income(income)
                    .expense(expense)
                    .net(net)
                    .cumulativeNet(cumulative)
                    .build());
        }
        return Dashboard.Cashflow.builder().granularity(granularity).buckets(buckets).build();
    }

    /** Divide el rango en ventanas por día, semana (lunes a domingo), mes o trimestre, recortadas al rango. */
    private List<Window> windows(PeriodRange range, Granularity granularity) {
        List<Window> windows = new ArrayList<>();
        LocalDate cursor = range.getFrom();
        while (!cursor.isAfter(range.getTo())) {
            LocalDate end = switch (granularity) {
                case DAY -> cursor;
                case WEEK -> cursor.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                case MONTH -> cursor.with(TemporalAdjusters.lastDayOfMonth());
                case QUARTER -> LocalDate.of(cursor.getYear(), ((cursor.getMonthValue() - 1) / 3) * 3 + 1, 1)
                        .plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
            };
            if (end.isAfter(range.getTo())) {
                end = range.getTo();
            }
            windows.add(new Window(cursor, end));
            cursor = end.plusDays(1);
        }
        return windows;
    }

    private String key(Granularity granularity, LocalDate from) {
        return switch (granularity) {
            case DAY, WEEK -> from.toString();                                            // 2026-10-24
            case MONTH -> YearMonth.from(from).toString();                                // 2026-10
            case QUARTER -> from.getYear() + "-Q" + ((from.getMonthValue() - 1) / 3 + 1); // 2026-Q4
        };
    }

    private Map<LocalDate, BigDecimal> toMap(List<DailyAmount> rows) {
        return rows.stream().collect(Collectors.toMap(DailyAmount::date, DailyAmount::amount, BigDecimal::add));
    }

    private BigDecimal sum(Map<LocalDate, BigDecimal> byDay, LocalDate from, LocalDate to) {
        return byDay.entrySet().stream()
                .filter(e -> !e.getKey().isBefore(from) && !e.getKey().isAfter(to))
                .map(Map.Entry::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ---------- Distribución por categoría ----------

    private Dashboard.Distribution buildDistribution(DashboardQuery query, PeriodRange range, PeriodRange previousRange,
                                                     MovementType type, Totals current, Totals previous) {
        boolean expense = type == MovementType.EXPENSE;
        BigDecimal total = expense ? current.expense() : current.income();
        BigDecimal previousTotal = expense ? previous.expense() : previous.income();

        List<CategoryAmount> rows = byCategory(query, range, type);
        List<CategoryAmount> previousRows = byCategory(query, previousRange, type);
        Map<String, BigDecimal> previousById = previousRows.stream()
                .collect(Collectors.toMap(CategoryAmount::categoryId, CategoryAmount::amount, BigDecimal::add));

        List<Dashboard.DistributionItem> items = new ArrayList<>();
        for (CategoryAmount row : rows) {
            items.add(item(row.categoryId(), row.name(), row.icon(), row.color(), row.amount(),
                    previousById.getOrDefault(row.categoryId(), BigDecimal.ZERO), total, current.income(), false));
        }

        // Movimientos sin categoría (datos anteriores a la migración a categorías)
        BigDecimal uncategorized = total.subtract(sumAmounts(rows));
        if (uncategorized.signum() > 0) {
            BigDecimal previousUncategorized = previousTotal.subtract(sumAmounts(previousRows)).max(BigDecimal.ZERO);
            items.add(item(null, "Sin categoría", "circle-help", "#6B7280", uncategorized,
                    previousUncategorized, total, current.income(), false));
        }

        items.sort(Comparator.comparing(Dashboard.DistributionItem::getAmount).reversed());
        int categoriesCount = items.size();

        return Dashboard.Distribution.builder()
                .type(type)
                .total(total)
                .categoriesCount(categoriesCount)
                .items(applyTopN(items, query.getTopN(), total, current.income()))
                .build();
    }

    private List<CategoryAmount> byCategory(DashboardQuery query, PeriodRange range, MovementType type) {
        return type == MovementType.EXPENSE
                ? expenseRepository.sumGroupedByCategory(query.getUserId(), query.getCurrency(), range.startDateTime(), range.endDateTime())
                : incomeRepository.sumGroupedByCategory(query.getUserId(), query.getCurrency(), range.startDateTime(), range.endDateTime());
    }

    private Dashboard.DistributionItem item(String categoryId, String name, String icon, String color,
                                            BigDecimal amount, BigDecimal previousAmount,
                                            BigDecimal total, BigDecimal totalIncome, boolean grouped) {
        return Dashboard.DistributionItem.builder()
                .categoryId(categoryId)
                .name(name)
                .icon(icon)
                .color(color)
                .amount(amount)
                .percentOfTotal(percent(amount, total))
                .percentOfIncome(percent(amount, totalIncome))
                .previousAmount(previousAmount)
                .variationVsPrevious(variation(amount, previousAmount))
                .grouped(grouped)
                .build();
    }

    private List<Dashboard.DistributionItem> applyTopN(List<Dashboard.DistributionItem> items, Integer topN,
                                                       BigDecimal total, BigDecimal totalIncome) {
        if (topN == null || items.size() <= topN) {
            return items;
        }
        List<Dashboard.DistributionItem> result = new ArrayList<>(items.subList(0, topN));
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal previousAmount = BigDecimal.ZERO;
        for (Dashboard.DistributionItem rest : items.subList(topN, items.size())) {
            amount = amount.add(rest.getAmount());
            previousAmount = previousAmount.add(rest.getPreviousAmount());
        }
        result.add(item(null, "Otras categorías", "ellipsis", "#9CA3AF", amount, previousAmount, total, totalIncome, true));
        return result;
    }

    private BigDecimal sumAmounts(List<CategoryAmount> rows) {
        return rows.stream().map(CategoryAmount::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal nz(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private BigDecimal percent(BigDecimal part, BigDecimal whole) {
        if (whole == null || whole.signum() == 0) {
            return null;
        }
        return part.multiply(HUNDRED).divide(whole, 2, RoundingMode.HALF_UP);
    }
    private BigDecimal variation(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.signum() == 0) {
            return null;
        }
        return current.subtract(previous).multiply(HUNDRED).divide(previous.abs(), 2, RoundingMode.HALF_UP);
    }
}