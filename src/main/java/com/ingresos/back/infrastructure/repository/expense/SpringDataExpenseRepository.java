package com.ingresos.back.infrastructure.repository.expense;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.infrastructure.entity.expense.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataExpenseRepository extends JpaRepository<ExpenseEntity, String> {

    List<ExpenseEntity> findByUserId(String userId);

    List<ExpenseEntity> findByAccountId(String accountId);

    List<ExpenseEntity> findByUserIdAndExpenseDateBetween(String userId, LocalDateTime start, LocalDateTime end);

    boolean existsByCategoryId(String categoryId);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e
        WHERE e.user.id = :userId AND e.category.id = :categoryId AND e.currency = :currency
          AND e.expenseDate BETWEEN :start AND :end
        """)
    BigDecimal sumByUserAndCategoryBetween(@Param("userId") String userId,
                                           @Param("categoryId") String categoryId,
                                           @Param("currency") Currency currency,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e
        WHERE e.user.id = :userId AND e.currency = :currency
          AND e.expenseDate BETWEEN :start AND :end
        """)
    BigDecimal sumByUserBetween(@Param("userId") String userId,
                                @Param("currency") Currency currency,
                                @Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);

    @Query("""
        SELECT EXTRACT(YEAR FROM e.expenseDate), EXTRACT(MONTH FROM e.expenseDate),
               EXTRACT(DAY FROM e.expenseDate), SUM(e.amount)
        FROM ExpenseEntity e
        WHERE e.user.id = :userId AND e.currency = :currency
          AND e.expenseDate BETWEEN :start AND :end
        GROUP BY EXTRACT(YEAR FROM e.expenseDate), EXTRACT(MONTH FROM e.expenseDate),
                 EXTRACT(DAY FROM e.expenseDate)
        """)
    List<Object[]> sumGroupedByDay(@Param("userId") String userId,
                                   @Param("currency") Currency currency,
                                   @Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end);

    @Query("""
        SELECT c.id, c.name, c.icon, c.color, SUM(e.amount)
        FROM ExpenseEntity e JOIN e.category c
        WHERE e.user.id = :userId AND e.currency = :currency
          AND e.expenseDate BETWEEN :start AND :end
        GROUP BY c.id, c.name, c.icon, c.color
        ORDER BY SUM(e.amount) DESC
        """)
    List<Object[]> sumGroupedByCategory(@Param("userId") String userId,
                                        @Param("currency") Currency currency,
                                        @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);
}
