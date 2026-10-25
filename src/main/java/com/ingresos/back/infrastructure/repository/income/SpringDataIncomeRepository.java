package com.ingresos.back.infrastructure.repository.income;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.infrastructure.entity.income.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataIncomeRepository extends JpaRepository<IncomeEntity, String> {

    List<IncomeEntity> findByUserId(String userId);

    List<IncomeEntity> findByAccountId(String accountId);

    List<IncomeEntity> findByUserIdAndIncomeDateBetween(String userId, LocalDateTime start, LocalDateTime end);

    boolean existsByCategoryId(String categoryId);

    @Query("""
        SELECT COALESCE(SUM(i.amount), 0) FROM IncomeEntity i
        WHERE i.user.id = :userId AND i.category.id = :categoryId AND i.currency = :currency
          AND i.incomeDate BETWEEN :start AND :end
        """)
    BigDecimal sumByUserAndCategoryBetween(@Param("userId") String userId,
                                           @Param("categoryId") String categoryId,
                                           @Param("currency") Currency currency,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    @Query("""
        SELECT COALESCE(SUM(i.amount), 0) FROM IncomeEntity i
        WHERE i.user.id = :userId AND i.currency = :currency
          AND i.incomeDate BETWEEN :start AND :end
        """)
    BigDecimal sumByUserBetween(@Param("userId") String userId,
                                @Param("currency") Currency currency,
                                @Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);

    @Query("""
        SELECT EXTRACT(YEAR FROM i.incomeDate), EXTRACT(MONTH FROM i.incomeDate),
               EXTRACT(DAY FROM i.incomeDate), SUM(i.amount)
        FROM IncomeEntity i
        WHERE i.user.id = :userId AND i.currency = :currency
          AND i.incomeDate BETWEEN :start AND :end
        GROUP BY EXTRACT(YEAR FROM i.incomeDate), EXTRACT(MONTH FROM i.incomeDate),
                 EXTRACT(DAY FROM i.incomeDate)
        """)
    List<Object[]> sumGroupedByDay(@Param("userId") String userId,
                                   @Param("currency") Currency currency,
                                   @Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end);

    @Query("""
        SELECT c.id, c.name, c.icon, c.color, SUM(i.amount)
        FROM IncomeEntity i JOIN i.category c
        WHERE i.user.id = :userId AND i.currency = :currency
          AND i.incomeDate BETWEEN :start AND :end
        GROUP BY c.id, c.name, c.icon, c.color
        ORDER BY SUM(i.amount) DESC
        """)
    List<Object[]> sumGroupedByCategory(@Param("userId") String userId,
                                        @Param("currency") Currency currency,
                                        @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);
}