package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.enums.category.CategoryType;
import com.ingresos.back.domain.exception.IncomeException;
import com.ingresos.back.domain.model.Income;
import com.ingresos.back.domain.port.AccountService;
import com.ingresos.back.domain.port.CategoryService;
import com.ingresos.back.domain.port.IncomeService;
import com.ingresos.back.domain.port.UseService;
import com.ingresos.back.domain.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UseService useService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Income createIncome(Income income) {
        validate(income);
        return incomeRepository.save(income);
    }

    @Override
    public Income findById(String id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeException("Ingreso no encontrado con ID: " + id));
    }

    @Override
    public List<Income> findAllIncomes() {
        return incomeRepository.findAll();
    }

    @Override
    public List<Income> findByUserId(String userId) {
        useService.findById(userId);
        return incomeRepository.findByUserId(userId);
    }

    @Override
    public List<Income> findByAccountId(String accountId) {
        accountService.findById(accountId);
        return incomeRepository.findByAccountId(accountId);
    }

    @Override
    public Income updateIncome(String id, Income income) {
        findById(id);
        validate(income);
        income.setId(id);
        return incomeRepository.save(income);
    }

    @Override
    public List<Income> findByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate) {
        useService.findById(userId);
        if (startDate == null || endDate == null) {
            throw new IncomeException("Las fechas mínima y máxima son obligatorias");
        }
        if (startDate.isAfter(endDate)) {
            throw new IncomeException("La fecha mínima no puede ser posterior a la fecha máxima");
        }
        return incomeRepository.findByUserIdAndIncomeDateBetween(
                userId, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
    }

    @Override
    public void deleteIncome(String id) {
        findById(id);
        incomeRepository.deleteById(id);
    }

    private void validate(Income income) {
        if (income.getAmount() == null || income.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IncomeException("El monto del ingreso debe ser mayor a cero");
        }
        useService.findById(income.getUserId());
        accountService.findById(income.getAccountId());
        if (income.getCategoryId() != null) {
            categoryService.findByIdForUser(income.getCategoryId(), income.getUserId(), CategoryType.INCOME);
        }
    }
}