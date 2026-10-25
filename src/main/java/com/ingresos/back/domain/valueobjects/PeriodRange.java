package com.ingresos.back.domain.valueobjects;

import com.ingresos.back.domain.enums.dashboard.Granularity;
import com.ingresos.back.domain.enums.dashboard.PeriodType;
import com.ingresos.back.domain.exception.DashboardException;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

@Getter
public final class PeriodRange {

    private static final long MAX_CUSTOM_DAYS = 366L * 5;

    private final PeriodType type;
    private final LocalDate from;
    private final LocalDate to;

    private PeriodRange(PeriodType type, LocalDate from, LocalDate to) {
        this.type = type;
        this.from = from;
        this.to = to;
    }


    public static PeriodRange of(PeriodType type, LocalDate reference, LocalDate from, LocalDate to) {
        if (type == null) {
            throw new DashboardException("El periodo es obligatorio");
        }
        if (type == PeriodType.CUSTOM) {
            if (from == null || to == null) {
                throw new DashboardException("Para un periodo personalizado, 'from' y 'to' son obligatorios");
            }
            if (from.isAfter(to)) {
                throw new DashboardException("La fecha mínima no puede ser posterior a la fecha máxima");
            }
            if (ChronoUnit.DAYS.between(from, to) + 1 > MAX_CUSTOM_DAYS) {
                throw new DashboardException("El rango personalizado no puede superar 5 años");
            }
            return new PeriodRange(type, from, to);
        }

        LocalDate ref = reference != null ? reference : LocalDate.now();
        return switch (type) {
            case WEEK -> {
                LocalDate monday = ref.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                yield new PeriodRange(type, monday, monday.plusDays(6));
            }
            case MONTH -> new PeriodRange(type, ref.withDayOfMonth(1), ref.with(TemporalAdjusters.lastDayOfMonth()));
            case QUARTER -> {
                LocalDate start = LocalDate.of(ref.getYear(), ((ref.getMonthValue() - 1) / 3) * 3 + 1, 1);
                yield new PeriodRange(type, start, start.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth()));
            }
            case YEAR -> new PeriodRange(type, LocalDate.of(ref.getYear(), 1, 1), LocalDate.of(ref.getYear(), 12, 31));
            case CUSTOM -> throw new IllegalStateException("CUSTOM se resuelve antes");
        };
    }

    public PeriodRange previous() {
        if (type == PeriodType.CUSTOM) {
            LocalDate previousTo = from.minusDays(1);
            return new PeriodRange(type, previousTo.minusDays(days() - 1), previousTo);
        }
        return of(type, from.minusDays(1), null, null);
    }

    public long days() {
        return ChronoUnit.DAYS.between(from, to) + 1;
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(from) && !date.isAfter(to);
    }

    public LocalDateTime startDateTime() {
        return from.atStartOfDay();
    }

    public LocalDateTime endDateTime() {
        return to.atTime(LocalTime.MAX);
    }

    public Granularity defaultGranularity() {
        return switch (type) {
            case WEEK -> Granularity.DAY;
            case MONTH -> Granularity.WEEK;
            case QUARTER, YEAR -> Granularity.MONTH;
            case CUSTOM -> days() <= 31 ? Granularity.DAY : days() <= 120 ? Granularity.WEEK : Granularity.MONTH;
        };
    }
}