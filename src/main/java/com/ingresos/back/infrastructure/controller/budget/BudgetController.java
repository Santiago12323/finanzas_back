package com.ingresos.back.infrastructure.controller.budget;

import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.domain.model.BudgetExpense;
import com.ingresos.back.domain.port.BudgetService;
import com.ingresos.back.infrastructure.controller.dto.BudgetDto;
import com.ingresos.back.infrastructure.controller.dto.BudgetExpenseDto;
import com.ingresos.back.infrastructure.mapper.BudgetDtoMapper;
import com.ingresos.back.infrastructure.mapper.BudgetExpenseDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetDtoMapper budgetDtoMapper;

    @Autowired
    private BudgetExpenseDtoMapper budgetExpenseDtoMapper;

    @PostMapping
    public ResponseEntity<BudgetDto> create(@RequestBody BudgetDto dto) {

        Budget budget = budgetDtoMapper.toDomain(dto);

        return new ResponseEntity<>(
                budgetDtoMapper.toDto(
                        budgetService.createBudget(budget, dto.getUserId())
                ),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetDto> findById(@PathVariable String id) {
        return budgetService.findById(id)
                .map(budgetDtoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetDto>> findByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(budgetService.findByUserId(userId).stream()
                .map(budgetDtoMapper::toDto)
                .toList()
        );
    }

    @GetMapping
    public ResponseEntity<List<BudgetDto>> findAll() {
        return ResponseEntity.ok(
                budgetService.findAll()
                        .stream()
                        .map(budgetDtoMapper::toDto)
                        .toList()
        );
    }

    @PostMapping("/{budgetId}/expenses")
    public ResponseEntity<BudgetDto> addExpense(
            @PathVariable String budgetId,
            @RequestBody BudgetExpenseDto dto
    ) {
        BudgetExpense expense = budgetExpenseDtoMapper.toDomain(dto);
        return ResponseEntity.ok(
                budgetDtoMapper.toDto(
                        budgetService.addExpense(budgetId, expense)
                )
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<BudgetDto> update(
            @PathVariable String id,
            @RequestBody BudgetDto dto
    ) {

        Budget budget = budgetDtoMapper.toDomain(dto);

        return ResponseEntity.ok(
                budgetDtoMapper.toDto(
                        budgetService.updateBudget(id, budget, dto.getUserId())
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        budgetService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{budgetId}/expenses/{expenseId}")
    public ResponseEntity<BudgetDto> removeExpense(
            @PathVariable String budgetId,
            @PathVariable String expenseId
    ) {
        return ResponseEntity.ok(
                budgetDtoMapper.toDto(
                        budgetService.removeExpense(budgetId, expenseId)
                )
        );
    }

}