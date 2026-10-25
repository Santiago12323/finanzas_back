package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.enums.budget.BudgetStatus;
import com.ingresos.back.domain.enums.category.CategoryType;
import com.ingresos.back.domain.exception.BudgetException;
import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.domain.port.AccountService;
import com.ingresos.back.domain.port.BudgetService;
import com.ingresos.back.domain.port.CategoryService;
import com.ingresos.back.domain.port.UseService;
import com.ingresos.back.domain.repository.BudgetRepository;
import com.ingresos.back.domain.repository.ExpenseRepository;
import com.ingresos.back.domain.valueobjects.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Service
public class BudgetServiceImpl implements BudgetService {

    private static final int DEFAULT_ALERT_THRESHOLD = 80;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UseService useService;

    @Autowired
    private AccountService accountService;

    @Override
    public Budget createBudget(Budget budget) {
        budget.setId(null);
        applyDefaults(budget);
        validate(budget);
        return enrich(budgetRepository.save(budget), null);
    }

    @Override
    public Budget findById(String id, YearMonth month) {
        return enrich(getBudget(id), month);
    }

    @Override
    public List<Budget> findByUserId(String userId, Boolean active, YearMonth month) {
        useService.findById(userId);
        return budgetRepository.findByUserId(userId).stream()
                .filter(b -> active == null || active.equals(b.getActive()))
                .map(b -> enrich(b, month))
                .toList();
    }

    @Override
    public Budget updateBudget(String id, Budget budget) {
        Budget existing = getBudget(id);
        budget.setId(id);
        budget.setUserId(existing.getUserId());   // el dueño no se puede cambiar
        applyDefaults(budget);
        validate(budget);
        return enrich(budgetRepository.save(budget), null);
    }

    @Override
    public void deleteBudget(String id) {
        getBudget(id);
        budgetRepository.deleteById(id);
    }


    private Budget getBudget(String id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetException("Presupuesto no encontrado con ID: " + id));
    }

    private void applyDefaults(Budget budget) {
        if (budget.getActive() == null) {
            budget.setActive(true);
        }
        if (budget.getAlertThreshold() == null) {
            budget.setAlertThreshold(DEFAULT_ALERT_THRESHOLD);
        }
    }

    private void validate(Budget budget) {
        if (budget.getName() == null || budget.getName().isBlank()) {
            throw new BudgetException("El nombre del presupuesto es obligatorio");
        }
        if (budget.getMonthlyLimit() == null
                || budget.getMonthlyLimit().getAmount() == null
                || budget.getMonthlyLimit().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BudgetException("El límite mensual debe ser mayor a cero");
        }
        if (budget.getMonthlyLimit().getCurrency() == null) {
            throw new BudgetException("La moneda del límite mensual es obligatoria");
        }
        if (budget.getAlertThreshold() < 1 || budget.getAlertThreshold() > 100) {
            throw new BudgetException("El porcentaje de alerta debe estar entre 1 y 100");
        }

        useService.findById(budget.getUserId());
        if (budget.getAccountId() != null) {
            accountService.findById(budget.getAccountId());
        }
        if (budget.getCategoryId() != null) {
            categoryService.findByIdForUser(budget.getCategoryId(), budget.getUserId(), CategoryType.EXPENSE);
        }

        // Un solo presupuesto activo por categoría (o un solo global) por usuario
        if (Boolean.TRUE.equals(budget.getActive())) {
            boolean duplicated = budgetRepository.findByUserId(budget.getUserId()).stream()
                    .anyMatch(b -> Boolean.TRUE.equals(b.getActive())
                            && !b.getId().equals(budget.getId())
                            && Objects.equals(b.getCategoryId(), budget.getCategoryId()));
            if (duplicated) {
                throw new BudgetException(budget.getCategoryId() == null
                        ? "Ya existe un presupuesto global activo"
                        : "Ya existe un presupuesto activo para esta categoría");
            }
        }
    }

    /** Calcula lo gastado en el mes, lo que queda, el porcentaje y el estado. */
    private Budget enrich(Budget budget, YearMonth month) {
        YearMonth target = month != null ? month : YearMonth.now();
        LocalDateTime start = target.atDay(1).atStartOfDay();
        LocalDateTime end = target.atEndOfMonth().atTime(LocalTime.MAX);

        Currency currency = budget.getMonthlyLimit().getCurrency();
        BigDecimal limit = budget.getMonthlyLimit().getAmount();

        BigDecimal spent = budget.getCategoryId() != null
                ? expenseRepository.sumByUserAndCategoryBetween(budget.getUserId(), budget.getCategoryId(), currency, start, end)
                : expenseRepository.sumByUserBetween(budget.getUserId(), currency, start, end);

        BigDecimal percent = spent.multiply(BigDecimal.valueOf(100))
                .divide(limit, 2, RoundingMode.HALF_UP);

        budget.setTotalSpent(new Balance(spent, currency));
        budget.setRemaining(limit.subtract(spent));
        budget.setPercentUsed(percent);

        if (percent.compareTo(BigDecimal.valueOf(100)) >= 0) {
            budget.setStatus(BudgetStatus.EXCEEDED);
        } else if (percent.compareTo(BigDecimal.valueOf(budget.getAlertThreshold())) >= 0) {
            budget.setStatus(BudgetStatus.WARNING);
        } else {
            budget.setStatus(BudgetStatus.OK);
        }

        budget.setCategoryName(budget.getCategoryId() != null
                ? categoryService.findById(budget.getCategoryId()).getName()
                : null);
        return budget;
    }
}